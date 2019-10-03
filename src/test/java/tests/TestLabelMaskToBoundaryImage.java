package tests;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import de.embl.cba.lazyalgorithm.view.NeighborhoodViews;
import ij.IJ;
import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import org.junit.Test;

public class TestLabelMaskToBoundaryImage
{
	@Test
	public < R extends RealType< R > > void run()
	{
		final ImagePlus imagePlus = IJ.openImage( TestLabelMaskToBoundaryImage.class.getResource( "../mask-lbl.zip" ).getFile() );

		final Img< R > wrap = ImageJFunctions.wrapReal( imagePlus );

		final BdvStackSource< R > image = BdvFunctions.show( wrap, "image", BdvOptions.options().is2D() );
		image.setDisplayRange( 0, 100 );

		final RandomAccessibleInterval< R > boundaryView = NeighborhoodViews.boundaryView( wrap, new long[]{ 2, 2 } );

		final BdvStackSource< R > boundary = BdvFunctions.show( boundaryView, "boundary", BdvOptions.options().is2D() );
		boundary.setDisplayRange( 0, 100 );
	}


	public static void main( String[] args )
	{
		new TestLabelMaskToBoundaryImage().run();
	}
}
