let logger = {};
["debug", "warn", "error"].forEach(v => {
    logger[v] = console[v]
})

export default logger
