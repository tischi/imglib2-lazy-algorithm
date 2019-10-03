package playground;

import de.embl.cba.neighborhood.RectangleShape2;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.converter.Converters;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;


/**
 * Class for demonstration and discussion purposes.
 * Is not actually used.
 *
 * @param <R>
 */
public class LazyFilterConverter< R extends RealType< R > & NativeType< R > >
{

	final RandomAccessibleInterval< R > rai;
	final long[] span;

	/**
	 * Provides an averaged filtered view on the input data.
	 *
	 * The regions which are averaged are span * 2 + 1, for each dimension.
	 * This ensures that the rectangle is symmetric around the central pixel.
	 */
	public LazyFilterConverter( RandomAccessibleInterval< R > input, long[] span )
	{
		this.rai = input;
		this.span = span;
	}

	public RandomAccessibleInterval< R > getView()
	{
		Shape shape = new RectangleShape2( span, false );

		final RandomAccessible< Neighborhood< R > > nra =
				shape.neighborhoodsRandomAccessible( Views.extendBorder( rai ) );

		final RandomAccessibleInterval< Neighborhood< R > > nrai = Views.interval( nra, rai );

		final RandomAccessibleInterval< R > averageView =
				Converters.convert( nrai,
						( neighborhood, output ) ->
						{
							setNeighborhoodFilterValue( neighborhood, output );
						},
						Util.getTypeFromInterval( rai ) );

		return averageView;
	}


	private static < R extends RealType< R > >
	void setNeighborhoodFilterValue( Neighborhood< R > neighborhood, R output )
	{
		double sum = 0;

		final Cursor< R > cursor = neighborhood.cursor();

		// one can figure out the position of the central pixel
		// neighborhood.localize(  ); // I assume this is the neighborhoods center location?

		while( cursor.hasNext() )
		{
			final R value = cursor.next();
			sum += value.getRealDouble();

			// one can also figure out the position of the other pixels
			// cursor.localize(  ); // This can be used to know the current position
		}

		// ...thus one can implement any kind of local neighborhood filter
		// one just needs to compute where the cursor is relative to the central pixel
		// e.g., median would be trivial, because one does not even need the positions

		// ...I assume this is not very efficient, because the neighborhoods are not
		// reused when going to the next pixel, one has to start the computation from scratch.
		// But for using it in the BDV it seems fast enough...I guess there is also multi-threading
		// during the computations, due to the way Bdv fetches the pixels

		output.setReal( sum / neighborhood.size() );
	}

}
