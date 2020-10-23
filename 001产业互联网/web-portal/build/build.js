import { resolve, copy } from "./utils"
export default () => {
	const config = require("./config/app.config")
	const compile = require("./compile")

	return compile.default(config.default).then(() => {
		copy(resolve("./static/"), resolve("./dist/release/"))
	})
}
