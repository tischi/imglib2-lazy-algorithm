package de.embl.cba.lazyalgorithm;

import de.embl.cba.lazyalgorithm.view.AverageView;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.SubsampleIntervalView;
import net.imglib2.view.Views;

import java.util.Arrays;

public class LazyDownsampler< R extends RealType< R > & NativeType< R > >
{

	final RandomAccessibleInterval< R > rai;
	final long[] span;

	public LazyDownsampler( RandomAccessibleInterval< R > rai, long[] span )
	{
		this.rai = rai;
		this.span = span;
	}

	public RandomAccessibleInterval< R > getDownsampledView()
	{
		final AverageView< R > averageView =
				new AverageView<>( rai, span );

		final long[] neighborhoodCenterDistance =
				Arrays.stream( span ).map( x -> 2 * x + 1 ).toArray();

		final SubsampleIntervalView< R > subsample = Views.subsample(
				averageView.averageView(), neighborhoodCenterDistance );

		return subsample;
	}

}
