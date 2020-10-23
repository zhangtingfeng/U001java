const babelRegister = require('@babel/register')
babelRegister({
    
})

if (process.argv.length > 2) {
    process.argv.splice(1, 1)
    module.exports = require(process.argv[1])
} else {
    console.warn("usage: node start.js [path]")
}