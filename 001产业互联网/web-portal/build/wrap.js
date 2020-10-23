import { logger, debug } from "./utils"

const debugMode = process.argv.length > 2 && "debug" === process.argv[2]
debug(debugMode)

Promise.resolve()
	.then(() => {
		if (debugMode) {
			logger.debug("========   debug    ========")
			const dev = require("./dev")
			return dev.default()
		} else {
			logger.debug("========  release  ========")
			const build = require("./build")
			return build.default()
		}
	})
	.then((msg) => {
		logger.debug(msg)
	})
	.catch((msg) => {
		logger.error("Failed\n:")
		logger.error(msg)
	})
