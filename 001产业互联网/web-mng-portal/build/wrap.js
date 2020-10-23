import {
    logger,
    debug
} from "./utils"

const debugMode = (process.argv.length > 2 && "debug" === process.argv[2])
debug(debugMode)

const dll = require("./dll")
Promise.resolve().then(() => {
    logger.debug("========   vendor   ========")
    return dll.default("vendor")
}).then(msg => {
    logger.debug(msg)
}).then(() => {
    logger.debug("======== framework  ========")
    return dll.default("framework")
}).then(msg => {
    logger.debug(msg)
}).then(() => {
    if (debugMode) {
        logger.debug("========   debug    ========")
        const dev = require("./dev")
        return dev.default()
    } else {
        logger.debug("========  release  ========")
        const build = require("./build")
        return build.default()
    }
}).then((msg) => {
    logger.debug(msg)
}).catch(msg => {
    logger.error("Failed\n:")
    logger.error(msg)
})