import moment from "moment";

/**
 * URL에서 쿼리스트링 파라미터 값을 가져오거나 기본값을 반환하는 함수입니다.
 *
 * @param {string} paramName - 쿼리스트링 파라미터 이름
 * @param {string} defaultValue - 기본값
 * @returns {string} - 쿼리스트링 파라미터 값 또는 기본값
 */
const getQueryParamOrDefault = (paramName, defaultValue) => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(paramName) || defaultValue;
};

/**
 * 날짜를 형식화된 문자열로 변환하는 함수입니다.
 *
 * @param {Date} createdAt - 변환할 날짜
 * @returns {string} - 형식화된 날짜 문자열
 */
const getFormattedDate = (createdAt) => {
  return moment(createdAt).format("YYYY.MM.DD HH:mm");
};
/**
 * util의 7일 이내인지 확인하는 함수입니다.
 * @param {string} createdAt - 게시글 생성일시
 * @returns {boolean} - 7일 이내이면 true, 그렇지 않으면 false
 */
const IsNewBoard = (createdAt) => {
  const createdDate = moment(createdAt);
  const sevenDaysAgo = moment().subtract(7, "days");
  return createdDate.isAfter(sevenDaysAgo);
};
export { getQueryParamOrDefault, getFormattedDate, IsNewBoard };