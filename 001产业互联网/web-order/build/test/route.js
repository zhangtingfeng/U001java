const AppPattern = /#!app\/(.+)$/
const ModulePattern = /#!m\/(.+)$/

let route = {
    comp: "",
    args: {}
}

function routieApp(app) {
    let m = /^([\w|\/|-]+)(\(([^\)]+)\))?/.exec(app)
    // let m = /^([\w|-]+)(\(([^\)]*)\))?(\?(.*))?$/.exec(app)
    if (m[1]) {
        route.comp = m[1]
        route.args = m[3] ? JSON.parse(m[3]) : m[3]
    }
}

function routieModule(app) {
    let m = /^([\w-]+)\/([\w-\/]+)(\?(.*))?$/.exec(app);
    if (m === null) return;
    let args = {};
    if (m[4]) {
        for (let t of m[4].split("&")) {
            let t1 = t.split("=");
            if (t1[0]) args[t1[0]] = t1[1];
        }
    }
    route.comp = "ModuleLoader";
    route.args = {app: [m[1], m[2]], args};
}

function routie() {
    let hash = decodeURIComponent(window.location.hash)
    let m = AppPattern.exec(hash)
    if (m) {
        routieApp(m[1], true)
        return;
    }

    m = ModulePattern.exec(hash)
    if (m) {
        routieModule(m[1])
        return;
    }
}

routie();
window.addEventListener("hashchange", event => {
    routie();
})


export default route;
