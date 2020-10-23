<template>
  <div>
    <a href="#">个人中心</a>
    <a href="order.html" class="active">订单中心</a>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
@import url("./common.less");

a {
  display: block;
  width: 200px;
  text-align: center;
  line-height: 30px;
  border-bottom: 1px solid @bg1;
  color: @color1;

  &.active {
    color: @text2;
    background-color: @color1;
  }
}
</style>