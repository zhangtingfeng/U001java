import webpackMerge from "webpack-merge"
import MiniCssExtractPlugin from "mini-css-extract-plugin"

import base from "./vendor.base"
import rules from "./config.rules"
import settings from "./config.settings"

let config = settings(webpackMerge({}, base, rules))

for (let rule of config.module.rules) {
  for (let u in rule.use) {
    if (rule.use[u] === "style-loader") {
      rule.use[u] = MiniCssExtractPlugin.loader
    }
  }
}

export default config