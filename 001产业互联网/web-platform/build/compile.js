import webpack from 'webpack'

export default (config) => {
    return new Promise((s, f) => {
        const compiler = webpack(config)
        compiler.run((err, stats) => {
            if (err) {
                f(err)
            } else if (stats.hasErrors()) {
                const msg = stats.toString({
                    chunks: false,
                    colors: true
                })
                f(msg)
            } else {
                s("finished.")
            }
        })
    })
}