package tests;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.IJ;
import ij.ImagePlus;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import org.junit.Test;

public class TestLabelMaskToBoundaryImage
{
	@Test
	public < T extends RealType< T > > void run()
	{
		final ImagePlus imagePlus = IJ.openImage( TestLabelMaskToBoundaryImage.class.getResource( "../mask-lbl.zip" ).getFile() );

		final Img< T > wrap = ImageJFunctions.wrapReal( imagePlus );

		final BdvStackSource< T > image = BdvFunctions.show( wrap, "image", BdvOptions.options().is2D() );
		image.setDisplayRange( 0, 100 );

	}


	public static void main( String[] args )
	{
		new TestLabelMaskToBoundaryImage().run();
	}
}
