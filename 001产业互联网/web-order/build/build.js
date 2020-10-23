import { copy } from "./utils"
export default () => {
	const config = require("./config")
	const compile = require("./compile")
	return compile.default(config.default).then(() => {
		copy("static", "dist/release")
	})
}
