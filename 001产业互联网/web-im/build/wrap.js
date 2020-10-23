import {
    logger,
    debug
} from "./utils"

const debugMode = (process.argv.length > 2 && "debug" === process.argv[2])
debug(debugMode)

Promise.resolve().then(() => {
    let app
    if (debugMode) {
        logger.debug("========   debug    ========")
        app = require("./dev")
    } else {
        logger.debug("========  release  ========")
        app = require("./build")
    }
    return app.default()
}).then((msg) => {
    logger.debug(msg)
}).catch(msg => {
    logger.error("Failed\n:")
    logger.error(msg)
})