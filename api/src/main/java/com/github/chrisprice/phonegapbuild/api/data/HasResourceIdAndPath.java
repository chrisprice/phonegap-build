package com.github.chrisprice.phonegapbuild.api.data;

import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

public interface HasResourceIdAndPath<T extends AbstractResource> extends HasResourceId<T>,
 HasResourcePath<T> {

}
