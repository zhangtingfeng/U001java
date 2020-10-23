<template>
  <div class="footer">
    <div class="footer-1">
      <div class="container">
        <template v-for="i of 4">
          <div class="item seprator" v-if="i &gt; 1" :key="`seprator${i}`"></div>
          <div class="item" :key="`item${i}`">
            <img :src="`imgs/main/icon_foot_${i}.svg`" />
            <div>
              <h4>{{ foot1[i - 1].title }}</h4>
              <p>{{ foot1[i - 1].text1 }}</p>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div class="footer-2" style="background: url(imgs/main/footer-bg.png)">
      <div class="container">
        <div class="item" v-for="i of foot2" :key="i.title">
          <h4>{{ i.title }}</h4>
          <p><a :href="i.href1" target="_blank">{{ i.text1 }}</a></p>
          <p><a :href="i.href2" target="_blank">{{ i.text2 }}</a></p>
          <p><a :href="i.href3" target="_blank">{{ i.text3 }}</a></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      foot1: [
        { title: "食品安全", text1: "精选生鲜 严格质检" },
        { title: "鲜活天然", text1: "绿色生态 汇农精选" },
        { title: "产地直采", text1: "限定产源 质量保证" },
        { title: "无忧售后", text1: "优鲜赔 售后通道" },
      ],
      foot2: [
        {
          title: "平台规则",
          text1: "平台服务协议", href1: "html/platform-service-agreement.html",
          text2: "隐私说明", href2: "html/privacy-statement.html",
          text3: "平台交易规则", href3: "html/platform-trading-rules.html"
        },
        { title: "新手上路", text1: "注册流程", text2: "热门问题", text3: "交易流程演示" },
        {
          title: "平台服务简介",
          text1: "交易服务简介", href1: "html/introduction-to-trading-services.html",
          text2: "仓储服务简介", href2: "html/introduction-to-warehousing-services.html",
          text3: "金融服务简介", href3: "html/introduction-to-financial-services.html",
        },
        {
          title: "合同模板",
          text1: "撮合合同模板", href1: "html/matching-contract-template.html",
          text2: "自营合同模板", href2: "html/self-supporting-contract-template.html",
          text3: "供应链金融合同模板", href3: "html/supply-chain-financial-contract-template.html",
        },
        { title: "支付方式", text1: "在线支付", text2: "余额支付", text3: "" },
      ],
    }
  },
}
</script>

<style lang="less" scoped>
.footer-1 {
  margin-top: 20px;
  border-top: 1px solid #ccc;
  background-color: #fff;
  .container {
    display: flex;
    flex-flow: row nowrap;

    .item {
      flex: 1 1 auto;
      display: flex;
      flex-flow: row nowrap;
      align-items: center;

      &.seprator {
        border-left: 1px solid #ccc;
      }

      img {
        flex: 0;
      }

      div {
        margin-left: 20px;
        flex: 1;
      }
    }
  }
}

.footer-2 {
  .container {
    display: flex;
    flex-flow: row nowrap;
    z-index: 100;

    .item {
      flex: 1 1 auto;
      color: #fff;
      p {
        font-size: 14px;
      }
    }
  }
}
.item {
  margin: 20px auto;
  font-size: 80%;
  h4 {
    margin: 5px auto;
  }
  p {
    margin: 3px auto;
  }
  a {
    color: #fff;
  }
  a:hover,
  a:focus {
    color: #fff;
  }
}
</style>
