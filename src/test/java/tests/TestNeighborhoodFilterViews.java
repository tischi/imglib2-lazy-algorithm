package tests;

import bdv.util.BdvFunctions;
import de.embl.cba.lazyalgorithm.view.NeighborhoodViews;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.Views;
import org.junit.Test;

import java.util.Random;

public class TestNeighborhoodFilterViews < R extends RealType< R > >
{
	public static void main( String[] args )
	{
		new TestNeighborhoodFilterViews().testBinnedView();
	}

	@Test
	public void testBinnedView()
	{
		final RandomAccessibleInterval< R > rai = createRandomImage();

		BdvFunctions.show( rai, "input" );

		final RandomAccessibleInterval< R > averageView =
				NeighborhoodViews.averageBinnedView( rai, new long[]{ 3, 3, 3 } );

		BdvFunctions.show( averageView, "binned" );
	}


	private static < R extends RealType< R > >
	RandomAccessibleInterval< R > createRandomImage()
	{
		final RandomAccessibleInterval< IntType > input = ArrayImgs.ints( 300, 300, 300 );

		final Random random = new Random( 10 );
		final Cursor< IntType > cursor = Views.iterable( input ).cursor();
		while ( cursor.hasNext() )
			cursor.next().set( random.nextInt( 65535 )  );

		return (RandomAccessibleInterval) input;
	}
}
