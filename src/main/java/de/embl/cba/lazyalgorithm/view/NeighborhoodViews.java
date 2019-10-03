package de.embl.cba.lazyalgorithm.view;

import de.embl.cba.lazyalgorithm.converter.NeighborhoodAverageConverter;
import de.embl.cba.lazyalgorithm.converter.NeighborhoodNonZeroBoundariesConverter;
import de.embl.cba.neighborhood.RectangleShape2;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.HyperSphereShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class NeighborhoodViews
{
	public static < R extends RealType< R > >
	RandomAccessibleInterval< R > averagedBinnedView(
			RandomAccessibleInterval< R > rai,
			long[] span )
	{
		return Views.subsample(
					NeighborhoodViews.rectangleAverageView( rai, span ),
					span );
	}


	/**
	 * Provides an averaged filtered view on the input data.
	 *
	 * The regions which are averaged are halfWidths * 2 + 1, for each dimension.
	 * This ensures that the rectangle is symmetric around the central pixel.
	 *
	 * TODO: also enable even kernels (also in BDT2) ?!
	 */
	public static < R extends RealType< R > >
	RandomAccessibleInterval< R > rectangleAverageView(
			RandomAccessibleInterval< R > rai,
			long[] halfWidths )
	{
		return neighborhoodConvertedView(
				rai,
				new NeighborhoodAverageConverter(),
				new RectangleShape2( halfWidths, false ) );
	}


	/**
	 * Provides an boundary filtered view on the input data.
	 * That is, only pixels where some pixel in the
	 * neighborhood has a different value are keeping their value.
	 * Other pixels are set to zero.
	 *
	 * TODO: also enable even kernels?!
	 */
	public static < R extends RealType< R > >
	RandomAccessibleInterval< R > nonZeroBoundariesView(
			RandomAccessibleInterval< R > rai,
			long radius )
	{
		return neighborhoodConvertedView(
				rai,
				new NeighborhoodNonZeroBoundariesConverter< R >( rai ),
				new HyperSphereShape( radius ) );
	}


	private static < R extends RealType< R > >
	RandomAccessibleInterval< R > neighborhoodConvertedView(
			RandomAccessibleInterval< R > rai,
			Converter< Neighborhood< R >, R > neighborhoodAverageConverter,
			Shape shape )
	{
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
