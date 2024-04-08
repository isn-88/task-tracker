'use strict';

let searchElement = document.getElementById('findParentInputModal');
searchElement.onkeyup = function () {
  findParentTaskByPattern(searchElement.value);
}

function findParentTaskByPattern(pattern = '') {
  fetch('/api/v1/tasks?' + new URLSearchParams({
    findPattern: pattern
  }))
  .then(function (response) {
    return response.json();
  })
  .then(function (responseData) {
    let html = '<ul class="list-group">';

    if (responseData.length > 0) {
      for (let count = 0; count < responseData.length; count++) {
        html += '<a href="javascript:;" class="list-group-item list-group-item-action parentIdCheckModal" ' +
          'id="parentId-' + responseData[count].id + '" ' +
          'onclick="checkParentTaskByResult(this)" ' +
          'value="' + responseData[count].id + '">' +
          responseData[count].type + ' #' + responseData[count].id +
          ': ' + responseData[count].title +
          '</a>';
      }
    }

    html += '</ul>';
    document.getElementById('searchParentResult').innerHTML = html;
  })
}

function checkParentTaskByResult(element) {
  let parentIdElement = document.getElementById('parentIdModal');
  parentIdElement.value = element.getAttribute('value');
}

function setParentId() {
  let parentIdModal = document.getElementById('parentIdModal');
  let parentIdInput = document.getElementById('editParent');
  parentIdInput.value = parentIdModal.value;
}














