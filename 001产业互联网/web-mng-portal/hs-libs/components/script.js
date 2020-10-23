const SCRIPTS = {};

export default function (url) {
    if (!SCRIPTS[url]) {
        SCRIPTS[url] = new Promise((resolve, reject) => {
            let $el = document.createElement("script")
            $el.src = url

            $el.addEventListener("load", (event) => {
                resolve()
            })
            $el.addEventListener("error", (event) => {
                reject(`load failed with url[${url}]`)
            })

            document.body.appendChild($el)
        })
    }

    return SCRIPTS[url]
}