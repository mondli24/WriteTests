document.querySelector('form').addEventListener('submit', function(event) {
    if (!validateLastQuestion()) {
        alert('Please complete the last question before submitting.');
        event.preventDefault();
        return;
    }

    const checkboxes = document.querySelectorAll('.correct-checkbox');
    let atLeastOneChecked = false;

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            checkbox.value = 'true';
            atLeastOneChecked = true;
        } else {
            const hiddenFalseInput = document.createElement('input');
            hiddenFalseInput.type = 'hidden';
            hiddenFalseInput.name = checkbox.name;
            hiddenFalseInput.value = 'false';
            checkbox.parentElement.appendChild(hiddenFalseInput);
        }
    });

    if (!atLeastOneChecked) {
        alert("At least one correct answer must be selected.");
        event.preventDefault();
    }
});

// Validation function to ensure all inputs are filled and at least one correct answer is selected
function validateLastQuestion() {
    const questions = document.querySelectorAll('.question-item');
    if (questions.length === 0) return true;

    const lastQuestion = questions[questions.length - 1];
    const inputs = lastQuestion.querySelectorAll('input[type="text"], select');
    for (let input of inputs) {
        if (!input.value) return false;
    }

    const questionTypeSelect = lastQuestion.querySelector('select[name*="questionType"]');
    if (questionTypeSelect.value === 'TRUE_FALSE') {
        const radioButtons = lastQuestion.querySelectorAll('input[type="radio"]');
        return Array.from(radioButtons).some(radio => radio.checked);
    } else {
        const correctCheckboxes = lastQuestion.querySelectorAll('input[type="checkbox"]');
        return Array.from(correctCheckboxes).some(checkbox => checkbox.checked);
    }
}

function addQuestion() {
    const container = document.getElementById('questions-container');
    if (!validateLastQuestion()) {
        alert('Please complete the last question before adding a new one.');
        return;
    }

    const questionCount = container.children.length;

    const questionHtml = `
        <div class="question-item">
            <label>Question:</label>
            <input type="text" name="questions[${questionCount}].questionText" required>
            <label>Type:</label>
            <select name="questions[${questionCount}].questionType" onchange="toggleAnswerType(this, ${questionCount})">
                <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                <option value="TRUE_FALSE">True/False</option>
            </select>
            <div class="answers-container" id="answers-container-${questionCount}">
            </div>
            <button type="button" onclick="addAnswer(${questionCount})">Add Answer</button>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', questionHtml);
    toggleAnswerType(document.querySelector(`select[name="questions[${questionCount}].questionType"]`), questionCount);
}

function toggleAnswerType(selectElement, questionIndex) {
    const answerContainer = document.getElementById(`answers-container-${questionIndex}`);
    answerContainer.innerHTML = '';

    const addAnswerButton = document.querySelector(`button[onclick='addAnswer(${questionIndex})']`);
    if (selectElement.value === 'TRUE_FALSE') {
        addAnswerButton.style.display = 'none'; // Hide the add answer button for True/False questions
        const trueFalseHtml = `
            <div>
                <label>True</label>
                <input type="radio" name="questions[${questionIndex}].correctAnswer" value="true" required>
                <label>False</label>
                <input type="radio" name="questions[${questionIndex}].correctAnswer" value="false" required>
            </div>
        `;
        answerContainer.insertAdjacentHTML('beforeend', trueFalseHtml);
    } else {
        addAnswerButton.style.display = 'block'; // Show the button for other types
    }
}

function addAnswer(questionIndex) {
    const container = document.getElementById(`answers-container-${questionIndex}`);
    if (!validateLastAnswer(container)) {
        alert('Please enter text for the last answer before adding a new one.');
        return;
    }

    const answerCount = container.children.length;

    const answerHtml = `
        <div class="answer-item">
            <label>Answer:</label>
            <input type="text" name="questions[${questionIndex}].answers[${answerCount}].answerText" required>
            <label>Correct:</label>
            <input type="checkbox" name="questions[${questionIndex}].answers[${answerCount}].isCorrect" class="correct-checkbox" value="true">
            <input type="hidden" name="questions[${questionIndex}].answers[${answerCount}].isCorrect" value="false">
        </div>
    `;
    container.insertAdjacentHTML('beforeend', answerHtml);
}

function validateLastAnswer(container) {
    const answers = container.querySelectorAll('.answer-item');
    if (answers.length === 0) return true;

    const lastAnswer = answers[answers.length - 1];
    const input = lastAnswer.querySelector('input[type="text"]');
    return input.value !== '';
}
