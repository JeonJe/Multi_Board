const getQueryParamOrDefault = (paramName, defaultValue) => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(paramName) || defaultValue;
};

export { getQueryParamOrDefault };
