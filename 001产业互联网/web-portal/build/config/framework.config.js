import webpackMerge from "webpack-merge"

import base from "./framework.base"
import rules from "./config.rules"
import settings from "./config.settings"

let config = settings(webpackMerge({}, base, rules))

export default config