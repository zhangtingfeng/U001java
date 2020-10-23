/**
 * 支持在html模板增加脚本和css
 * options:
 *  {
 *      version: string
 *      js: array
 *      css: array
 *  }
 */

import HtmlWebpackPlugin from "html-webpack-plugin"

const NAME = "HtmlInjectPlugin";

class HtmlInjectPlugin {
    constructor(opt) {
        this.opt = Object.assign({}, opt)
    }
    apply(compiler) {
        // HtmlWebpackPlugin >= 4.0
        if (HtmlWebpackPlugin.getHooks) {
            compiler.hooks.compilation.tap(NAME, (compilation) => {
                HtmlWebpackPlugin.getHooks(compilation).alterAssetTagGroups.tapAsync(
                    NAME,
                    (data, cb) => {
                        this.proc4(data)
                        cb(null, data)
                    }
                )
            });
        }
        // HtmlWebpackPlugin < 4.0
        else {
            compiler.hooks.compilation.tap(NAME, (compilation) => {
                const evt = "html-webpack-plugin-alter-asset-tags"
                // const evt = "html-webpack-plugin-before-html-processing"
                compilation.plugin(evt, data => {
                    this.proc3(data)
                });
            });
        }
    }

    procOptions() {
        let body = [],
            head = []

        for (let n of this.opt.js || [])
            body = body.concat({
                tagName: "script",
                closeTag: true,
                attributes: {
                    type: "text/javascript",
                    src: this.procVersion(n)
                }
            })
        for (let n of this.opt.css || [])
            head = head.concat({
                tagName: "link",
                closeTag: false,
                attributes: {
                    rel: "stylesheet",
                    href: this.procVersion(n)
                }
            })
        return { head, body }
    }

    proc3(data) {
        let tmp = this.procOptions();
        data.body = tmp.body.concat(data.body)
        data.head = tmp.head.concat(data.head)
    }

    proc4(data) {
        let tmp = this.procOptions();
        data.bodyTags = tmp.body.concat(data.bodyTags)
        data.headTags = tmp.head.concat(data.headTags)
    }

    procVersion(n) {
        if (this.opt.version) {
            return /\?/.test(n) ? `${n}&${this.opt.version}` : `${n}?${this.opt.version}`
        } else
            return n
    }
}

export default HtmlInjectPlugin;
