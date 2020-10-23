<template>
  <nav class="header">
    <div class="container">
      <img src="imgs/logo.svg" width="188px" height="85px"/>
      <h4>欢迎登录汇农苹果交易平台</h4>
    </div>
  </nav>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
.header {
  background-color: #fff;
  .container {
    display: flex;
    flex-flow: row wrap;
    justify-content: flex-start;

    img,
    h4 {
      flex-flow: 0;
    }

    h4 {
      margin-left: 30px;
      margin-top: 40px;
      color: #666;
      font-size: 18px;
    }
  }
}
</style>