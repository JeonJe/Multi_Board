<template>
  <form class="form-inline mb-4" @submit.prevent="clickSearch">
    <!-- 시작일 -->
    <input
      type="date"
      id="startDate"
      name="startDate"
      class="form-control mr-2"
      v-model="searchCondition.startDate"
    />
    <!-- 종료일 -->
    <input
      type="date"
      id="endDate"
      name="endDate"
      class="form-control mr-2"
      v-model="searchCondition.endDate"
    />
    <!-- 카테고리 -->
    <select v-model="searchCondition.categoryValue" class="form-control">
      <option
        v-for="categoryOption in categoryOptions"
        :key="categoryOption.value"
        :value="categoryOption.value"
      >
        {{ categoryOption.label }}
      </option>
    </select>
    <!-- 검색어 -->
    <input
      type="text"
      id="searchText"
      name="searchText"
      class="form-control mr-2"
      placeholder="제목 or 내용"
      v-model="searchCondition.searchText"
    />
    <!-- 페이지 크기 -->
    <select
      class="from-control mr-2"
      v-model="searchCondition.pageSize"
      @change="changeListViewCondition"
    >
      <option value="10">10개씩 보기</option>
      <option value="20">20개씩 보기</option>
      <option value="30">30개씩 보기</option>
      <option value="40">40개씩 보기</option>
      <option value="50">50개씩 보기</option>
    </select>
    <!-- 정렬 기준 -->
    <select
      class="from-control mr-2"
      v-model="searchCondition.sortCriteria"
      @change="changeListViewCondition"
    >
      <option value="createdAt">등록일시</option>
      <option value="title">제목</option>
      <option value="visitCount">조회수</option>
    </select>
    <!-- 정렬 순서 -->
    <select
      class="from-control mr-2"
      v-model="searchCondition.orderBy"
      @change="changeListViewCondition"
    >
      <option value="desc">내림차순</option>
      <option value="asc">오름차순</option>
    </select>

    <button type="submit" class="btn btn-primary">검색</button>
  </form>
</template>

<script>
import moment from "moment";
import { getQueryParamOrDefault } from "@/utils/util";

export default {
  props: {
    categories: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      /**
       * 검색 조건을 담는 객체
       */
      searchCondition: this.createDefaultSearchCondition(),
    };
  },
  computed: {
    /**
     * 카테고리 select-option 목록을 생성합니다.
     */
    categoryOptions() {
      const defaultOption = { value: "", label: "전체" };
      const options = this.categories.map((category) => ({
        value: category.categoryValue,
        label: category.categoryName,
      }));
      return [defaultOption, ...options];
    },
  },
  mounted() {
    // 컴포넌트가 마운트되면 기본 검색 조건을 이벤트로 전달합니다.
    this.$emit("emitSearchContion", this.searchCondition);
  },
  methods: {
    /**
     * 검색 버튼 클릭 이벤트 핸들러입니다.
     * 현재 검색 조건을 부모 컴포넌트에 이벤트로 전달합니다.
     */
    clickSearch() {
      this.$emit("emitSearchContion", this.searchCondition);
    },
    /**
     * 리스트 뷰 조건 변경 이벤트 핸들러입니다.
     * 현재 검색 조건을 이벤트로 전달합니다.
     */
    changeListViewCondition() {
      this.$emit("emitSearchContion", this.searchCondition);
    },
    /**
     * 기본 검색 조건 객체를 생성합니다.
     * @returns {object} - 기본 검색 조건 객체
     */
    createDefaultSearchCondition() {
      const now = moment().format("YYYY-MM-DD");
      const oneYearsAgo = moment().subtract(1, "year").format("YYYY-MM-DD");
      //URL 쿼리스트링에 검색어 조건이 있으면 추출하고 없으면 기본값을 사용합니다.
      const searchCondition = {
        searchText: getQueryParamOrDefault("searchText", ""),
        startDate: getQueryParamOrDefault("startDate", oneYearsAgo),
        endDate: getQueryParamOrDefault("endDate", now),
        currentPage: getQueryParamOrDefault("currentPage", "1"),
        pageSize: getQueryParamOrDefault("pageSize", "10"),
        offset: getQueryParamOrDefault("offset", "0"),
        sortCriteria: getQueryParamOrDefault("sortCriteria", "createdAt"),
        orderBy: getQueryParamOrDefault("orderBy", "desc"),
        categoryValue: getQueryParamOrDefault("categoryValue", ""),
      };
      return searchCondition;
    },
  },
};
</script>
