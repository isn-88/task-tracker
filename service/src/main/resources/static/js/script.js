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

    if (responseData.success) {
      let data = responseData.data;
      if (data.length > 0) {
        for (let count = 0; count < data.length; count++) {
          html += '<a href="javascript:;" class="list-group-item list-group-item-action parentIdCheckModal" ' +
            'id="parentId-' + data[count].id + '" ' + 'onclick="selectParentTaskByResult(this)" ' +
            'title="' + data[count].id + '">' + data[count].type + ' #' +
            data[count].id + ': ' + data[count].title + '</a>';
        }
      }
    }

    html += '</ul>';
    document.getElementById('searchParentResult').innerHTML = html;
  })
}

function selectParentTaskByResult(element) {
  let parentIdElement = document.getElementById('parentIdModal');
  parentIdElement.value = element.title;
}

function setParentId() {
  let parentIdModal = document.getElementById('parentIdModal');
  let parentIdInput = document.getElementById('editParent');
  parentIdInput.value = parentIdModal.value;
}














