package com.google.zxing.decode;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 From bitmap decoding
 * 
 * @author hugo
 * 
 */
public class BitmapDecoder {

	MultiFormatReader multiFormatReader;

	public BitmapDecoder(Context context) {

		multiFormatReader = new MultiFormatReader();

		// Decoded parameters
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(
				2);
		// You can parse encoding type
		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();

			// Here the type of scan settings , I choose here the support
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		// Continue to set the character encoding format to UTF8
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

		// Setting Resolution Configuration parameters
		multiFormatReader.setHints(hints);

	}

	/**
	 * Get the decoding result
	 * 
	 * @param bitmap
	 * @return
	 */
	public Result getRawResult(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		try {
			return multiFormatReader.decodeWithState(new BinaryBitmap(
					new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
		}
		catch (NotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
