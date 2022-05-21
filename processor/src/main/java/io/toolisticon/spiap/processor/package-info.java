/**
 * This package contains the annotation processors.
 */
@AnnotationWrapper(
        value = {Service.class, Services.class, SpiService.class, SpiServices.class, SpiServiceLocator.class, SpiServiceLocators.class, Spi.class},
        customInterfaces = {
                @CustomInterface(annotationToWrap=Service.class, interfacesToApply = {ServiceAnnotation.class}),
                @CustomInterface(annotationToWrap=SpiService.class, interfacesToApply = {ServiceAnnotation.class})
        },
        usePublicVisibility = true
)
package io.toolisticon.spiap.processor;

import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper;
import io.toolisticon.aptk.annotationwrapper.api.CustomInterface;
import io.toolisticon.spiap.api.Service;
import io.toolisticon.spiap.api.Services;
import io.toolisticon.spiap.api.Spi;
import io.toolisticon.spiap.api.SpiService;
import io.toolisticon.spiap.api.SpiServiceLocator;
import io.toolisticon.spiap.api.SpiServiceLocators;
import io.toolisticon.spiap.api.SpiServices;
