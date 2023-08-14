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
/**
 * downloadAttachment 함수는 첨부 파일 ID를 사용하여 첨부 파일 다운로드 URL을 생성합니다.
 *
 * @param attachmentId 첨부 파일 ID
 * @return 다운로드 URL
 */
const downloadAttachment = (attachmentId) => {
  return `${process.env.VUE_APP_API_SER_URL}${process.env.VUE_APP_API_FILE_DOWNLOAD}/${attachmentId}`;
};
/**
 * 제목의 유효성을 검사하는 함수
 * @param {string} title - 검사할 제목 문자열
 * @returns {Promise<boolean>} - 제목의 유효성 여부를 Promise로 반환 (true: 유효, false: 유효하지 않음)
 */
const validateTitle = async (title) => {
  if (title.length >= 100 || title.length <= 0) {
    return false;
  }
  return true;
};
/**
 * 내용의 유효성을 검사하는 함수
 * @param {string} content - 검사할 내용 문자열
 * @returns {Promise<boolean>} - 내용의 유효성 여부를 Promise로 반환 (true: 유효, false: 유효하지 않음)
 */
const validateContent = async (content) => {
  if (content.length >= 4000 || content.length <= 0) {
    return false;
  }
  return true;
};
/**
 * 첨부 파일의 유효성을 검사하는 함수 (2MB이하의 jpg,jif,png,zip파일 허용)
 * @param {File[]} files - 검사할 첨부 파일 배열
 * @returns {Promise<boolean>} - 첨부 파일의 유효성 여부를 Promise로 반환 (true: 유효, false: 유효하지 않음)
 */
const validateFiles = async (files) => {
  const allowedExtensions = ["jpg", "jif", "png", "zip"];
  const maxFileSize = 2 * 1024 * 1024;

  for (const file of files) {
    const fileExtension = file.name.split(".").pop().toLowerCase();

    if (!allowedExtensions.includes(fileExtension) || file.size > maxFileSize) {
      return false;
    }
  }

  return true;
};
/**
 * 이미지 유효성을 검사하는 비동기 함수
 * @param {Array} images - 유효성을 검사할 이미지 파일 배열
 * @returns {Promise<boolean>} - 이미지 유효성 여부 (true: 유효, false: 유효하지 않음)
 */
const validateImages = async (images) => {
  const allowedExtensions = ["jpg", "jif", "png"];
  const maxFileSize = 1 * 1024 * 1024;

  for (const image of images) {
    const imageExtension = image.name.split(".").pop().toLowerCase();

    if (
      !allowedExtensions.includes(imageExtension) ||
      image.size > maxFileSize
    ) {
      return false;
    }
  }

  return true;
};

export {
  getQueryParamOrDefault,
  getFormattedDate,
  IsNewBoard,
  downloadAttachment,
  validateTitle,
  validateContent,
  validateFiles,
  validateImages,
};
