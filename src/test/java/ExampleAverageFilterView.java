import bdv.util.BdvFunctions;
import de.embl.cba.lazyalgorithm.converter.AverageFilterConverter;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.Views;

import java.util.Random;

public class ExampleAverageFilterView
{
	public static < R extends RealType< R > >
	void main( String[] args )
	{
		final RandomAccessibleInterval< R > rai = createRandomImage();

		BdvFunctions.show( rai, "input" );

		final RandomAccessibleInterval< R > averageView =
				 new AverageFilterConverter<>( rai, new long[]{ 5, 5, 5 } ).averageView();

		BdvFunctions.show( averageView, "average" );
	}


	private static < R extends RealType< R > >
	RandomAccessibleInterval< R > createRandomImage()
	{
		final RandomAccessibleInterval< IntType > input = ArrayImgs.ints( 300, 300, 300 );

		final Random random = new Random( 10 );
		final Cursor< IntType > cursor = Views.iterable( input ).cursor();
		while ( cursor.hasNext() )
		{
			cursor.next().set( random.nextInt( 65535 )  );
		}

		return (RandomAccessibleInterval) input;
	}
}
