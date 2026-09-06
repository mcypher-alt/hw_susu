document.getElementById('contactForm').addEventListener('submit', function(event) {
  event.preventDefault();

  const name = document.getElementById('userName').value;
  const email = document.getElementById('userEmail').value;

  console.log('🧪 --- НОВАЯ ЗАЯВКА HYPERLABS --- 🧪');
  console.log('Имя подопытного:', name);
  console.log('Почта для связи:', email);
  console.log('------------------------------------');

  this.style.display = 'none';
  
  document.getElementById('successMessage').classList.remove('d-none');
});

document.getElementById('dosageForm').addEventListener('submit', function(event) {
  event.preventDefault();

  const weight = parseInt(document.getElementById('userWeight').value, 10);
  const drug = document.getElementById('drugSelect').value;
  const dose = (weight % 10) * 100;

  const resultDiv = document.getElementById('dosageResult');
  resultDiv.innerHTML = `
    <div class="small text-uppercase tracking-wide mb-1 text-success-emphasis">Протокол утвержден</div>
    <div class="fs-4 fw-bold mb-1">${drug}: <span class="text-muted">${dose} мг/нед</span></div>
    <div class="small opacity-75">Принимать строго во время гипертрофической фазы луны.</div>
  `;
  resultDiv.classList.remove('d-none');
});