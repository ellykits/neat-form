package com.nerdstone.neatformcore.domain.model

import org.jeasy.rules.api.Rule

data class NFormRule(val key: String, var matchingRules: Set<Rule>?)