const appSettings = require("./appsetting")

const presets = [
  [
    "@babel/env",
    {
      targets: {
        browsers: ["last 2 versions"]
      },
      useBuiltIns: "usage",
      corejs: 3
    },
  ],
];

const plugins = [
  "@babel/plugin-transform-runtime",
  "@babel/plugin-syntax-dynamic-import",
  "@babel/plugin-proposal-class-properties",
  /*
  [
    "babel-plugin-import",
    {
      "libraryName": "ant-design-vue",
      "libraryDirectory": "lib",
      "style": "css"
    }
  ]
  */
]

if (appSettings.modules.ie) {
  presets[0][1].targets.browsers.push("ie >= 9")
}

module.exports = {
  presets,
  plugins
};