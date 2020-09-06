package com.nerdstone.neatformcore.utils

import com.nerdstone.neatformcore.domain.model.NFormViewProperty

fun NFormViewProperty.getStyleFromAttribute(): String? = viewAttributes?.get("style")?.toString()