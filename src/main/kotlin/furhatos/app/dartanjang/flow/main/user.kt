package furhatos.app.dartanjang.flow.main

import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

var User.tmb: Int by NullSafeUserDataDelegate { 20 }
var User.lost: Boolean by NullSafeUserDataDelegate { false }
var User.polite: Boolean by NullSafeUserDataDelegate { true }
var User.nPress: Int by NullSafeUserDataDelegate { 0 }