package de.embl.cba.lazyalgorithm.converter;

import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.type.numeric.RealType;

public class NeighborhoodBoundaryConverter< R extends RealType< R > >
		implements Converter< Neighborhood< R >, R >
{
	@Override
	public void convert( Neighborhood< R > neighborhood, R output )
	{
		final R centerValue = neighborhood.cursor().get();

		for ( R value : neighborhood )
			if ( ! value.equals( centerValue ) )
			{
				// there is a value in the neighborhood that is not equal
				// to the centre pixel, thus the centre pixel is a
				// boundary pixel
				output.set( centerValue );
				return;
			}

		output.setZero();
	}
}
