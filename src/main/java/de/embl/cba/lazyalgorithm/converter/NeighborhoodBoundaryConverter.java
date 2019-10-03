package de.embl.cba.lazyalgorithm.converter;

import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;

public class NeighborhoodBoundaryConverter< R extends RealType< R > >
		implements Converter< Neighborhood< R >, R >
{
	private final RandomAccessibleInterval< R > rai;

	public NeighborhoodBoundaryConverter( RandomAccessibleInterval< R > rai )
	{
		this.rai = rai;
	}

	@Override
	public void convert( Neighborhood< R > neighborhood, R output )
	{
		final double centerValue = getCenterValue( neighborhood );

		for ( R value : neighborhood )
		{
			if ( value.getRealDouble() != centerValue )
			{
				output.setReal( centerValue );
				return;
			}
		}

		output.setZero();
	}

	private double getCenterValue( Neighborhood< R > neighborhood )
	{
		long[] centrePosition = new long[ neighborhood.numDimensions() ];
		neighborhood.localize( centrePosition );

		final RandomAccess< R > randomAccess = rai.randomAccess();
		randomAccess.setPosition( centrePosition );
		return randomAccess.get().getRealDouble();
	}
}
