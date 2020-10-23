export default () => {
    const config = require("./config")
    const compile = require("./compile")
    return compile.default(config.default)
}