package com.afba.imageplus.utilities;

import com.mst.viewer.mstech.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MSBatchConverter {
	private long[] conversionInfo;

	public long convertFile(String[] args) {
		try {
			if (args.length == 6) {
				String src = args[0]; // Source Document folder path.

				String dest = args[1]; // Destination Document folder path.

				String srcExt = args[2];

				String destExt = args[3]; // Destination conversion extension e.g. [tiff/pdf]

				String annotationInfo = args[4];
				boolean burnAnnotation = false;
				boolean savaAnnotation = false;
				boolean excludeAnnotation = false;
				boolean isAnnColor = false;

				if (annotationInfo.equalsIgnoreCase("1"))
					burnAnnotation = true;
				else if (annotationInfo.equalsIgnoreCase("2"))
					burnAnnotation = isAnnColor = true;
				else if (annotationInfo.equalsIgnoreCase("3"))
					savaAnnotation = true;
				else if (annotationInfo.equalsIgnoreCase("4"))
					excludeAnnotation = true;

				Converter conv = new Converter();
				String isConvertDirStructure = args[5];

				if (isConvertDirStructure.equalsIgnoreCase("0")) {
					conversionInfo = conv.convertWithoutDirStructure(src, dest, srcExt, destExt, burnAnnotation,
							savaAnnotation, excludeAnnotation, isAnnColor, annotationInfo);
				} else if (isConvertDirStructure.equalsIgnoreCase("1"))
					conversionInfo = conv.convertWithDirStructure(src, dest, srcExt, destExt, burnAnnotation,
							savaAnnotation, excludeAnnotation, isAnnColor, annotationInfo);

				log.info("Total files converted: {}", conversionInfo[0]);
				log.info("Total files failed to convert: {}", conversionInfo[1]);
				log.info("Total time taken to convert: {} sec", conversionInfo[2]);

				return conversionInfo[0];
			} else {
				log.error("Enter All The Parameters !!!");
			}

		}

		catch (Exception e) {
			log.error(e.getMessage());
		}
		return 0;

	}

}
