import { resolve, copy } from "./utils"
export default () => {
    const config = require("./config/app.config")
    const compile = require("./compile")

    copy(resolve("./static/"), resolve("./dist/release/"))

    return compile.default(config.default)
}