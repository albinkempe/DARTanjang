package furhatos.app.dartanjang.flow.main

import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

var User.tmb by NullSafeUserDataDelegate { 20 }
var User.lost by NullSafeUserDataDelegate { false }