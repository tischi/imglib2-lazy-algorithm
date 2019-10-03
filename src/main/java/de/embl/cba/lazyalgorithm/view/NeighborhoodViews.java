package de.embl.cba.lazyalgorithm.view;

import de.embl.cba.lazyalgorithm.converter.NeighborhoodAverageConverter;
import de.embl.cba.lazyalgorithm.converter.NeighborhoodBoundaryConverter;
import de.embl.cba.neighborhood.RectangleShape2;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class NeighborhoodViews
{

	/**
	 * Provides an averaged filtered view on the input data.
	 *
	 * The regions which are averaged are span * 2 + 1, for each dimension.
	 * This ensures that the rectangle is symmetric around the central pixel.
	 *
	 * TODO: also enable even kernels (also in BDT2) ?!
	 */
	public static < R extends RealType< R > >
	RandomAccessibleInterval< R > averageView(
			RandomAccessibleInterval< R > rai,
			long[] span )
	{
		return neighborhoodConvertedView(
				rai,
				span,
				new NeighborhoodAverageConverter() );

	}



	/**
	 * Provides an boundary filtered view on the input data.
	 * That is, only pixels where some pixel in the
	 * neighborhood has a different value are keeping their value.
	 * Other pixels are set to zero.
	 *
	 * The regions which are averaged are span * 2 + 1, for each dimension.
	 * This ensures that the rectangle is symmetric around the central pixel.
	 *
	 * TODO: also enable even kernels (also in BDT2) ?!
	 */
	public static < R extends RealType< R > >
	RandomAccessibleInterval< R > boundaryView(
			RandomAccessibleInterval< R > rai,
			long[] span )
	{
		return neighborhoodConvertedView(
				rai,
				span,
				new NeighborhoodBoundaryConverter< R >( rai ) );
	}


	private static < R extends RealType< R > >
	RandomAccessibleInterval< R > neighborhoodConvertedView(
			RandomAccessibleInterval< R > rai,
			long[] span,
			Converter< Neighborhood< R >, R > neighborhoodAverageConverter )
	{
		Shape shape = new RectangleShape2( span, false );

		final RandomAccessible< Neighborhood< R > > nra =
				shape.neighborhoodsRandomAccessible(
						Views.extendBorder( rai ) );

		final RandomAccessibleInterval< Neighborhood< R > > nrai
				= Views.interval( nra, rai );

		return Converters.convert( nrai,
				neighborhoodAverageConverter,
				Util.getTypeFromInterval( rai ) );
	}

}
