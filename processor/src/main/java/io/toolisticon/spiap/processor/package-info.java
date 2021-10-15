/**
 * This package contains the annotation processors.
 */
@AnnotationWrapper({Service.class, Services.class, SpiServiceLocator.class, Spi.class})
package io.toolisticon.spiap.processor;

import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;
import io.toolisticon.spiap.api.Spi;
import io.toolisticon.spiap.api.SpiServiceLocator;