let router = {
  comp: "singin"
}

const AppPattern = /#!([^\/]+)\/?(.*)$/
function routie() {
  let hash = decodeURIComponent(window.location.hash)
  let m = AppPattern.exec(hash)
  if (m) {
    router.comp = m[1]
  }
}

routie();
window.addEventListener("hashchange", event => {
  routie();
})

export default router;