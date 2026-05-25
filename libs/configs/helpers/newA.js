const applyFilters = () => {
  window.requestAnimationFrame(() => {
    if (isStepsLessThen1500()) {
      startLoading();
      window.requestAnimationFrame(() => {
        // get selected filter and create filterArray
        let selectedFilters = Array.from(
          document.querySelectorAll("span.demo:not(.outline)")
        );
        let filtersArray = [];
        selectedFilters.forEach((filter) =>
          filtersArray.push(filter.getAttribute("title"))
        );

        // iterate over all the child and decide dispay according to filter
        let resultTableTrChildrens = Array.from(
          document.querySelectorAll(
            "div#test-details-wrapper table.table-results tr td[alt]"
          )
        );
        resultTableTrChildrens.forEach((resultTableTrChildren) => {
          let stepTR = resultTableTrChildren.parentNode;
          let collapse = true;
          if (filtersArray.length) {
            if (resultTableTrChildren.hasAttribute("title")) {
              stepTR.style.display = filtersArray.includes(
                resultTableTrChildren.getAttribute("title")
              )
                ? "table-row"
                : "none";
            } else {
              // for started and ened who does not have the title attribute create object to decide its display for respective filter
              const logAttributes = {
                fail: resultTableTrChildren.getAttribute("fail"),
                warning: resultTableTrChildren.getAttribute("warning"),
                error: resultTableTrChildren.getAttribute("error"),
                info: resultTableTrChildren.getAttribute("info"),
              };
              if (
                Object.values(logAttributes).every((value) => value === null)
              ) {
                stepTR.style.display = "none";
              } else {
                for (const key in logAttributes) {
                  if (logAttributes[key]) {
                    stepTR.style.display = filtersArray.includes(key.toString())
                      ? "table-row"
                      : "none";
                    if (stepTR.style.display == "table-row") break;
                  }
                }
              }
            }
          } else {
            stepTR.style.display = "table-row";

            // to keep the arrow down and everything expanded in case of clear filter or no filter selected
            let statusTD = stepTR.querySelectorAll(
              "td[class='step-details']"
            )[0];
            if (statusTD.innerText.includes("### Started")) {
              wrapStartStatusIcon(
                stepTR.querySelectorAll("td[class*='status']")[0],
                "keyboard_arrow_down"
              );
              stepTR.setAttribute("wrapped", "false");
              collapse = false;
            }
          }

          if (collapse) {
            // for the wrapping of the steps work proper
            let statusTD = stepTR.querySelectorAll(
              "td[class='step-details']"
            )[0];
            if (statusTD.innerText.includes("### Started")) {
              wrapStartStatusIcon(
                stepTR.querySelectorAll("td[class*='status']")[0],
                "chevron_right"
              );
              stepTR.setAttribute("wrapped", "true");
            }
          }
        });
      });
      window.requestAnimationFrame(() => {
        endLoading();
      });
    } else {
      startLoading();
      window.requestAnimationFrame(() => {
        // get selected filter and create filterArray
        let selectedFilters = Array.from(
          document.querySelectorAll("span.demo:not(.outline)")
        );
        let filtersArray = [];
        selectedFilters.forEach((filter) =>
          filtersArray.push(filter.getAttribute("title"))
        );

        // iterate over all the child and decide dispay according to filter
        let resultTableTrChildrens = Array.from(
          document.querySelectorAll(
            "div#test-details-wrapper table.table-results tr td[alt]"
          )
        );
        resultTableTrChildrens.forEach((resultTableTrChildren) => {
          let stepTR = resultTableTrChildren.parentNode;
          if (filtersArray.length) {
            stepTR.style.display = filtersArray.includes(
              resultTableTrChildren.getAttribute("title")
            )
              ? "table-row"
              : "none";
          } else {
            stepTR.style.display = "table-row";
          }
        });
      });
      window.requestAnimationFrame(() => {
        endLoading();
      });
    }
  });
}; // end of applyFilters

// remove all filter incldueding wrap
const resetFilterStyles = () => {
  let selectedFilters = Array.from(
    document.querySelectorAll("span.demo:not(.outline)")
  );
  selectedFilters.forEach((selectedFilter) => {
    selectedFilter.classList.toggle("outline");
    selectedFilter.children[0].classList.toggle("text-white");
  });
};

// apply selected filter to the every child tr
const updateFiltersOnClick = (target) => {
  // in case of wrap and clear filter first remove every filter.
  if (
    target.getAttribute("title") == "clear filters" ||
    target.getAttribute("title") == "wrap"
  ) {
    resetFilterStyles();

    // for the wrap filter call function wrap all
    if (target.getAttribute("title") == "wrap") {
      wrapAll();
    }
  } else {
    target.classList.toggle("outline");
    target.children[0].classList.toggle("text-white");
  }
};

// inital point
window.onload = async () => {
  // Create the parent div
  const checkBoxParent = document.createElement("div");
  checkBoxParent.className = "checkBoxParent";
  // Create the label element
  const label = document.createElement("label");
  label.className = "switch";
  label.title = "Show scenario for applied filter";
  // create message element

  // Create the input element
  const input = document.createElement("input");
  input.id = "checkbox";
  input.type = "checkbox";
  input.setAttribute("onclick", "switchClick()");
  // Create the span element
  const span = document.createElement("span");
  span.className = "sliderx round";
  // Append the input and span elements to the label
  label.appendChild(input);
  label.appendChild(span);
  // Append the label to the parent div
  checkBoxParent.appendChild(label);
  // get the search element
  let reportSearch = document.getElementsByClassName("search")[0];
  // add element checkBoxParent after search
  reportSearch.insertAdjacentElement("afterend", checkBoxParent);

  // for adding loading
  const spinner = document.createElement("div");
  spinner.setAttribute("id", "cover-spin");
  spinner.setAttribute("style", "display: none;");
  const testView = document.getElementById("test-view");
  testView.parentNode.insertBefore(spinner, testView);

  // create the floating button and all filter and add it to the html
  let filterResultsDiv = document.createElement("div");
  filterResultsDiv.innerHTML =
    '<div class="fixed-action-btn" id="floatingButton"><a class="btn-floating blue"><i class="large mdi-action-subject icon"></i></a><ul>' +
    '<li><span class="label info outline demo" title="info"><i class="mdi-action-info-outline"></i></span></li>' +
    '<li><span class="label warning outline demo" title="warning"><i class="mdi-alert-warning"></i></span></li>' +
    '<li><span class="label fail outline demo" title="fail"><i class="mdi-navigation-cancel"></i></span></li>' +
    '<li><span class="label error outline demo" title="error"><i class="mdi-alert-error"></i></span></li>' +
    '<li><span class="label other outline demo" title="wrap"><i class="mdi-editor-wrap-text"></i></span></li>' +
    '<li><span class="label capitalize unknown outline demo" title="clear filters"><i class="mdi-navigation-close"></i></li>';
  ("</ul></div>");
  let cardPanelDiv = document.querySelector(
    "#test-details-wrapper div.card-panel"
  );
  cardPanelDiv.insertBefore(filterResultsDiv, cardPanelDiv.childNodes[0]);
  let floatingButtonDiv = document.querySelector("div#floatingButton");
  floatingButtonDiv.style.bottom = "30px";
  floatingButtonDiv.style.right = "50px";
  let floatingButtonUl = document.querySelector("div#floatingButton ul");
  floatingButtonUl.style.bottom = "44px";
  // get the current selected filter from the event from the click
  let spans = Array.from(document.querySelectorAll("span.demo"));
  spans.forEach((span) => {
    span.style.cursor = "pointer";
    span.style.fontSize = "1.125em";
    span.addEventListener("click", (e) => {
      let currentTarget = e.target.classList.contains("demo")
        ? e.target
        : e.target.parentElement;

      updateFiltersOnClick(currentTarget);
      applyFilters();

      // in case of wrap first apply filter and then update the filter as it will remove all other filter
      if (
        currentTarget.getAttribute("title") == "wrap" &&
        isStepsLessThen1500()
      ) {
        updateFiltersOnClick(currentTarget);
      }
    });
  });

  let collectionItems = Array.from(
    document.querySelectorAll(".collection-item")
  );
  collectionItems.forEach((collectionItem) =>
    collectionItem.addEventListener("click", resetFilterStyles)
  );

  //Shows only last occurrence of test with testFilter. testFilter can be error, fail, warning, ...
  const filterTests = (testFilter) => {
    //getting all fail test names
    let allTests = Array.from(
      document.querySelectorAll(`ul#test-collection li span.test-name`)
    );
    let testsToShow = {};
    //looping through all tests
    allTests.forEach((test) => {
      // getting test name
      let testName = test.innerText.split("- ReRun")[0].trim();
      // getting the test element
      let testLi = test.parentElement.parentElement;
      // filtering tests which fails till last rerun
      if (testLi.classList.contains(testFilter)) {
        testsToShow[testName] = testLi;
      } else {
        delete testsToShow[testName];
      }
      //hiding all tests
      testLi.classList.remove("displayed");
      testLi.classList.add("hide");
    });
    //showing only filtered tests
    for (test in testsToShow) {
      testsToShow[test].classList.add("displayed");
      testsToShow[test].classList.remove("hide");
    }
    //opening first filtered test case
    let displayedTests = Array.from(
      document.querySelectorAll(`li.${testFilter}.displayed`)
    );
    if (displayedTests.length) displayedTests[0].click();
  }; // end of filterTests

  //getting the last number of rerun
  let rerunNumber = Number(
    Array.from(document.querySelectorAll("span.test-name"))
      .pop()
      .innerText.split(" ")
      .pop()
  );
  //showing last rerun failures only when rerun is present else showing normal failures
  if (rerunNumber) {
    ["fail", "warning", "error"].forEach((testFilter) => {
      //getting the testFilter element
      let failTestFilter = document.querySelector(
        `ul#tests-toggle li.${testFilter}`
      );
      if (failTestFilter) {
        //removing class attribute to stop the extent.js logic
        failTestFilter.removeAttribute("class");
        //showing only filtered tests
        failTestFilter.addEventListener("click", () => {
          filterTests(testFilter);
        });
      }
    });
  }

  // make variable change to enable displaye filter at start
  let applyFailWarnFilter = true;
  if (applyFailWarnFilter) {
    // select fail and warn scenarios
    let failTests = Array.from(
      document.querySelectorAll("li.collection-item.test.fail")
    );
    let warnTests = Array.from(
      document.querySelectorAll("li.collection-item.test.warning")
    );
    if (failTests.length > 0) {
      // select failed scenarios from filter
      document.querySelectorAll("ul#tests-toggle li.fail")[0].click();

      // select warning scenarios if available
      if (warnTests.length > 0) {
        // make warning active in filter
        if (
          document.querySelectorAll("ul#tests-toggle li.warning").length > 0
        ) {
          document
            .querySelectorAll("ul#tests-toggle li.warning")[0]
            .setAttribute("class", "warning active");
        }
        // display warning scenarios
        warnTests.forEach((test) => {
          // getting class name
          let testClassName = test.getAttribute("class");
          // update class to get displayed
          testClassName = testClassName.replace("hide", "displayed");
          // setting class name
          test.setAttribute("class", testClassName);
        });
      }
    } else if (warnTests.length > 0) {
      // select failed scenarios from filter
      if (document.querySelectorAll("ul#tests-toggle li.warning").length > 0) {
        document.querySelectorAll("ul#tests-toggle li.warning")[0].click();
      }
    }
  } // end of filter at start script

  /** For steps wrapping**/
  // for icons
  const attachStylesheetForIcon = () => {
    document.head.innerHTML =
      '<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">' +
      document.head.innerHTML;
  };
  attachStylesheetForIcon();

  // onclick on the left hand side scenarios
  let testsTR = document.querySelectorAll("#test-collection > li");
  testsTR.forEach((test) => {
    test.setAttribute("onclick", "attachOnClickInStarted()");
  });

  // mark all the started
  markAllStarted();

  // call for first time
  attachOnClickInStarted();

  // clicking first filtered scenario
  window.requestAnimationFrame(() => {
    document.querySelector("li.collection-item.displayed").click();
  });

  // filtering the fail steps on opening failed test cases
  Array.from(document.querySelectorAll("li.collection-item.test.fail")).forEach(
    (scenario) => {
      scenario.addEventListener("click", () => {
        window.requestAnimationFrame(() => {
          document.querySelector("span.label.fail.demo").click();
        });
      });
    }
  );
}; // end of on load

const markAllStarted = () => {
  let startLogs = document.querySelectorAll("tr:has(b[class='coresteplog'])");

  startLogs.forEach((log) => {
    const arrow_down = document.createElement("i");
    arrow_down.setAttribute("class", "material-icons");
    arrow_down.setAttribute(
      "style",
      "font-weight: bold;color: black; font-size: large;"
    );
    arrow_down.innerHTML = "keyboard_arrow_down";

    log.setAttribute("onclick", "wrapStartBlock(this)");
    log.getElementsByClassName("status info")[0].removeAttribute("title");

    // add arrow down icon at beginning
    let statusTD = log.querySelectorAll("td[class*='status']")[0];

    statusTD.insertBefore(arrow_down, statusTD.firstChild);
    statusTD.getElementsByClassName("mdi-action-info-outline")[0].remove();
  });

  // remove title from the ended logs
  let endLogs = document.querySelectorAll("tr:has(b[class='coresteplogE'])");
  endLogs.forEach((log) => {
    log.getElementsByClassName("status info")[0].removeAttribute("title");
    log.setAttribute("onclick", "endedClick(this)");
  });
}; //end of markAllStarted;

// attach onclick on started steps
const attachOnClickInStarted = async () => {
  // get the check box value and decide flow
  const checkBox = document.getElementById("checkbox");
  if (checkBox.checked == true && isStepsLessThen1500()) {
    window.requestAnimationFrame(() => {
      startLoading();
      window.requestAnimationFrame(() => {
        let startEndLogs = document.querySelectorAll(
          "#test-details-wrapper tr:has(b[class='coresteplog'])"
        );
        markStartedEndedNEW(startEndLogs, 0, false);
        window.requestAnimationFrame(() => {
          endLoading();
        });
      });
    });
  }

  if (isStepsLessThen1500()) {
    // document.getElementsByClassName("sliderx round")[0].style.display = "block";
    document.getElementsByClassName("switch")[0].style.display = "block";
  } else {
    // document.getElementsByClassName("sliderx round")[0].style.display = "none";
    document.getElementsByClassName("switch")[0].style.display = "none";
  }
}; // end of attachOnClickInStarted

const isStepsLessThen1500 = () => {
  if (document.querySelectorAll("#test-details-wrapper tr").length < 1500)
    return true;
  else return false;
};

/**
 * On radio switch clicking click on the current selected to go through the each scenario once
 * Before : get all the filter
 * After : apply all filter
 */
const switchClick = () => {
  if (isStepsLessThen1500()) {
    // get the applied filter before click
    let selectedFilters = Array.from(
      document.querySelectorAll("span.demo:not(.outline)")
    );

    // click on the same test case to again iterate thorugh each scenario
    document.getElementsByClassName("collection-item active")[0].click();

    // apply filter again
    selectedFilters.forEach((filter) => {
      if (!(filter.title == "fail")) {
        filter.click();
      }
    });
  } else {
    alert("Functionality is not available for steps more then 1500");
  }
}; // end of switchClick

const startLoading = () => {
  $("#cover-spin").css("display", "block");
};

const endLoading = () => {
  $("#cover-spin").css("display", "none");
};

// mark fail info warrning error in the started and ended and remvoe the title attribute from the.
const markStartedEndedNEW = (startEndLogs, start, innerStarted) => {
  for (let i = start; i < startEndLogs.length; i++) {
    let tempAttribute = "temp" + i;
    const currentStarted = startEndLogs[i];
    currentStarted.setAttribute(tempAttribute, "");
    const precedingSiblingTRs = document.querySelectorAll(
      `#test-details-wrapper tr[${tempAttribute}]:has(b[class='coresteplog']) ~ tr`
    );

    let shouldReturn = false;
    let data;
    let logAttributes = {};
    let startCount = 0;
    let logScnearioCount = 0;

    for (let j = 0; j < precedingSiblingTRs.length; j++) {
      logScnearioCount++;
      pTR = precedingSiblingTRs[j];
      let currentPreecingTRText =
        pTR.getElementsByClassName(`step-details`)[0].innerText;

      let logLevel = pTR
        .querySelectorAll("td[class*='status']")[0]
        .getAttribute("title");

      // to skip further trs after wrapped the needed block
      if (currentPreecingTRText.includes("### Started")) {
        startCount++;
        data = markStartedEndedNEW(startEndLogs, i + startCount, true);

        if (data) {
          if (data.innerStarted) innerStarted = data.innerStarted;
          i = i + data.startCount;
          j = j + data.logScnearioCount;
          logScnearioCount = logScnearioCount + data.logScnearioCount;

          // set the logAttributes in the ended
          if (data.fail === "true") {
            currentStarted
              .getElementsByClassName("status info")[0]
              .setAttribute("fail", true);
          }
          if (data.warning === "true") {
            currentStarted
              .getElementsByClassName("status info")[0]
              .setAttribute("warning", true);
          }
          if (data.error === "true") {
            currentStarted
              .getElementsByClassName("status info")[0]
              .setAttribute("error", true);
          }
          if (data.info === "true") {
            currentStarted
              .getElementsByClassName("status info")[0]
              .setAttribute("info", true);
          }
        }
      } else if (currentPreecingTRText.includes("### Ended")) {
        logAttributes = {
          fail: currentStarted
            .getElementsByClassName("status info")[0]
            .getAttribute("fail"),
          warning: currentStarted
            .getElementsByClassName("status info")[0]
            .getAttribute("warning"),
          error: currentStarted
            .getElementsByClassName("status info")[0]
            .getAttribute("error"),
          info: currentStarted
            .getElementsByClassName("status info")[0]
            .getAttribute("info"),
        };
        // set the logAttributes in the ended
        if (logAttributes.fail === "true") {
          pTR
            .getElementsByClassName("status info")[0]
            .setAttribute("fail", true);
        }
        if (logAttributes.warning === "true") {
          pTR
            .getElementsByClassName("status info")[0]
            .setAttribute("warning", true);
        }
        if (logAttributes.error === "true") {
          pTR
            .getElementsByClassName("status info")[0]
            .setAttribute("error", true);
        }
        if (logAttributes.info === "true") {
          pTR
            .getElementsByClassName("status info")[0]
            .setAttribute("info", true);
        }
        shouldReturn = true;
        break;
      } else {
        // mark the started
        if (
          !currentStarted
            .getElementsByClassName("step-details")[0]
            .textContent.includes("### Ended")
        ) {
          switch (logLevel) {
            case "fail":
              currentStarted
                .getElementsByClassName("status info")[0]
                .setAttribute("fail", true);
              break;

            case "warning":
              currentStarted
                .getElementsByClassName("status info")[0]
                .setAttribute("warning", true);
              break;

            case "info":
              currentStarted
                .getElementsByClassName("status info")[0]
                .setAttribute("info", true);
              break;

            case "error":
              currentStarted
                .getElementsByClassName("status info")[0]
                .setAttribute("error", true);
              break;
          }
        }
      }
    }
    currentStarted.removeAttribute(tempAttribute);
    if (innerStarted && shouldReturn) {
      logAttributes.logScnearioCount = logScnearioCount;
      logAttributes.startCount = startCount;
      return logAttributes;
    }
  }
}; // end of markStartedEndedNEW

// method to show the given icon in started block status
const wrapStartStatusIcon = (startedStatusTD, iconName) => {
  startedStatusTD.getElementsByClassName("material-icons")[0].innerText =
    iconName;
};

/**
 * Simulate the start click on clicking on the ended
 * @param {*} clickedTR
 */
const endedClick = (clickedTR) => {
  console.log("endedClick is called");
  // get the nearest started block and click on it
  let element = clickedTR.previousElementSibling;

  let startCount = 0,
    endCount = 1;

  while (element) {
    const text = element.getElementsByClassName("step-details")[0].textContent;
    if (text.includes("### Started")) {
      startCount++;
    } else if (text.includes("### Ended")) {
      endCount++;
    }
    if (startCount == endCount) {
      element.click();
      break;
    } else {
      element = element.previousElementSibling;
    }
  }
}; //end of endedClick

// method to be called onclick
const wrapStartBlock = (clickedTR) => {
  let currentTRText =
    clickedTR.getElementsByClassName("coresteplog")[0].innerText;

  // add attribute for identifying
  clickedTR.setAttribute("current", "");

  // for deciding show hide
  let displayValue =
    clickedTR.getAttribute("wrapped") != "true" ? "none" : "table-row";

  // counters of started & ended logs to track the shwo hide
  let startCount = 1,
    endCount = 0;

  let precedingSiblingTRs = document.querySelectorAll(
    "#test-details-wrapper tr[current]:has(b[class='coresteplog']) ~ tr"
  );

  // go through all tr to identify the needed ones
  precedingSiblingTRs.forEach((pTR) => {
    let currentPreecingTRText =
      pTR.getElementsByClassName(`step-details`)[0].innerText;
    // to skip further trs after wrapped the needed block
    if (startCount == endCount) return;

    if (currentPreecingTRText.includes("### Started")) {
      startCount++;
      if (startCount > 1) {
        pTR.style.display = displayValue;
      }
      pTR.removeAttribute("wrapped");
      wrapStartStatusIcon(pTR, "keyboard_arrow_down");
    } else if (currentPreecingTRText.includes("### Ended")) {
      endCount++;
      if (startCount > endCount) {
        pTR.style.display = displayValue;
      }
    } else {
      // for showing failure & warning the condition else perform on all
      let logLevel = pTR
        .querySelectorAll("td[class*='status']")[0]
        .getAttribute("title");
      if (!(logLevel == "fail" || logLevel == "warning")) {
        pTR.style.display = displayValue;
      }
    }
  });

  // Remove attribute to identify the next tr
  clickedTR.removeAttribute("current");

  // update wrapped value
  clickedTR.setAttribute("wrapped", displayValue == "none" ? true : false);

  let iconName =
    displayValue == "none" ? "chevron_right" : "keyboard_arrow_down";

  wrapStartStatusIcon(
    clickedTR.querySelectorAll("td[class*='status']")[0],
    iconName
  );
};

const wrapAll = () => {
  if (isStepsLessThen1500()) {
    window.requestAnimationFrame(() => {
      startLoading();

      window.requestAnimationFrame(() => {
        const startedTR = document.querySelectorAll(
          "#test-details-wrapper tr:has(b[class='coresteplog'])"
        );

        startedTR.forEach((currentTR) => {
          // add attribute for identifying
          currentTR.setAttribute("current", "");
          let displayValue = "none";

          // counters of started & ended logs to track the shwo hide
          let startCount = 1,
            endCount = 0;

          let precedingSiblingTRs = document.querySelectorAll(
            `#test-details-wrapper tr[current]:has(b[class='coresteplog']) ~ tr`
          );

          // go through all tr to identify the needed ones
          precedingSiblingTRs.forEach((pTR) => {
            let currentPreecingTRText =
              pTR.getElementsByClassName(`step-details`)[0].innerText;

            if (startCount == endCount) return;

            if (currentPreecingTRText.includes("### Started")) {
              startCount++;
              if (startCount > 1) {
                pTR.style.display = displayValue;
              }
              wrapStartStatusIcon(
                pTR.querySelectorAll("td[class*='status']")[0],
                "chevron_right"
              );
            } else if (currentPreecingTRText.includes("### Ended")) {
              endCount++;
              if (startCount > endCount) {
                pTR.style.display = displayValue;
              }
            } else {
              // for showing failure & warning the condition else perform on all
              let logLevel = pTR
                .querySelectorAll("td[class*='status']")[0]
                .getAttribute("title");
              if (!(logLevel == "fail" || logLevel == "warning")) {
                pTR.style.display = displayValue;
              }
            }
          });

          // Remove attribute to identify the next tr
          currentTR.removeAttribute("current");

          // update wrapped value
          currentTR.setAttribute("wrapped", "true");
          wrapStartStatusIcon(
            currentTR.querySelectorAll("td[class*='status']")[0],
            "chevron_right"
          );
        });
      });
      window.requestAnimationFrame(() => {
        endLoading();
      });
    });
  } else {
    alert("Functionality is not available for steps more then 1500");
  }
};

// // READ MORE BUTTON
//Conditions for three differnet types of table - test case description, prerequisite and steps description
const conditionsForButton = (getAllTable, element) => {
  var getTable;

  if (getAllTable.length == 2) {
    getTable = element.querySelector(".desc-table:nth-of-type(2)");
  } else if (getAllTable.length == 3) {
    getTable = element.querySelector(".desc-table:nth-of-type(3)");
  } else {
    getTable = element.querySelector(".desc-table:nth-of-type(1)");
  }

  return getTable;
};

//When loading the report, use display none if the count exceeds five.
const countMoreThanFive = (lenghtOfTrTag, getTrTag) => {
  if (lenghtOfTrTag > 5) {
    for (var iterator = 1; iterator <= lenghtOfTrTag; iterator++) {
      if (iterator > 5) {
        getTrTag[iterator].style.display = "none";
      }
    }
  }
};

const onLoadFiveSteps = () => {
  var detailsContainerClass = document.querySelector(".details-container");

  var testDesc = detailsContainerClass.querySelector(".test-desc");

  var getAllTable = testDesc.querySelectorAll(".desc-table");

  var getTable = conditionsForButton(getAllTable, testDesc);

  var getScenarioCountTag = testDesc.querySelector("#stepsCount");

  var totalCount = getScenarioCountTag.querySelector("span").id;

  var lenghtOfTrTag = parseInt(totalCount);

  var getTrTag = getTable.getElementsByTagName("tr");

  countMoreThanFive(lenghtOfTrTag, getTrTag);
};

const onClickFiveSteps = (event) => {
  var element = event.target.closest("li");

  var getAllTable = element.querySelectorAll(".desc-table");

  var getTable = conditionsForButton(getAllTable, element);

  var getScenarioCountTag = element.querySelector("#stepsCount");

  var totalCount = getScenarioCountTag.querySelector("span").id;

  var lenghtOfTrTag = parseInt(totalCount);

  var getTrTag = getTable.getElementsByTagName("tr");

  countMoreThanFive(lenghtOfTrTag, getTrTag);
};

// When the javascript loads, this will execute
var getAllTestCollections = document.querySelector(".test-collection");
let getCollectionItem =
  getAllTestCollections.querySelectorAll("li.collection-item");
for (i = 0; i < getCollectionItem.length; i++) {
  getCollectionItem[i].addEventListener("click", onLoadFiveSteps);
  getCollectionItem[i].addEventListener("click", onClickFiveSteps);
}

//Called when the 'Read More' or 'Read Less' button clicked
clickReadButton = (button) => {
  var detailsContainerClass = document.querySelector(".details-container");

  var getAllTable = detailsContainerClass.querySelectorAll(".desc-table");

  var getTable = conditionsForButton(getAllTable, detailsContainerClass);

  var getScnarioCountTag = detailsContainerClass.querySelector("#stepsCount");

  var totalCount = getScnarioCountTag.querySelector("span").id;

  var lenghtOfTrTag = parseInt(totalCount);

  var getTrTag;

  for (var iterator = 1; iterator <= lenghtOfTrTag; iterator++) {
    getTrTag = getTable.getElementsByTagName("tr")[iterator];
    if (iterator > 5 && getTrTag.style.display == "") {
      getTrTag.style.display = "none";
    } else {
      getTrTag.style.display = "";
    }
  }

  if (lenghtOfTrTag > 5 && getTrTag.style.display == "") {
    button.innerHTML = "Read Less";
  } else {
    button.innerHTML = "Read More";
  }
};
