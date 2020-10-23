import {
    debug
} from "./utils"
debug(false)

import compile from "./compile"
const config = require("./config/app.config")

compile(config.default)