package de.embl.cba.lazyalgorithm.view;

import de.embl.cba.lazyalgorithm.converter.NeighborhoodAverageConverter;
import de.embl.cba.neighborhood.RectangleShape2;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class AverageView< R extends RealType< R > >
{

	final RandomAccessibleInterval< R > rai;
	final long[] span;

	/**
	 * Provides an averaged filtered view on the input data.
	 *
	 * The regions which are averaged are span * 2 + 1, for each dimension.
	 * This ensures that the rectangle is symmetric around the central pixel.
	 */
	public AverageView( RandomAccessibleInterval< R > input, long[] span )
	{
		this.rai = input;
		this.span = span;
	}


	public RandomAccessibleInterval< R > averageView()
	{
		Shape shape = new RectangleShape2( span, false );

		final RandomAccessible< Neighborhood< R > > nra =
				shape.neighborhoodsRandomAccessible( Views.extendBorder( rai ) );

		final RandomAccessibleInterval< Neighborhood< R > > nrai
				= Views.interval( nra, rai );

		final NeighborhoodAverageConverter neighborhoodAverageConverter
				= new NeighborhoodAverageConverter();

		final RandomAccessibleInterval< R > averageView =
				Converters.convert( nrai,
						neighborhoodAverageConverter,
						Util.getTypeFromInterval( rai ) );

		return averageView;
	}

}