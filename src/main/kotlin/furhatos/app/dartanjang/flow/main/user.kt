package furhatos.app.dartanjang.flow.main

import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

var User.tmb: Int by NullSafeUserDataDelegate { 0 }
var User.polite: Boolean by NullSafeUserDataDelegate { true }
var User.nPress: Int by NullSafeUserDataDelegate { 0 }
var User.dieSum: Int by NullSafeUserDataDelegate { 0 }