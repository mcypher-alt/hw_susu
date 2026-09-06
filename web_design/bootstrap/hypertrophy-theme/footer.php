<div class="modal fade" id="dosageModal" tabindex="-1" aria-labelledby="dosageModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content border-slate-800 bg-slate-950 text-light rounded-4 shadow-lg overflow-hidden">
        <div class="modal-header border-slate-800 px-4 pt-4 pb-3">
          <div>
            <span class="badge bg-success-subtle text-success border border-success-subtle mb-1">Лабораторный калькулятор</span>
            <h4 class="modal-title fw-bold" id="dosageModalLabel">Подбор протокола</h4>
          </div>
          <button type="button" class="btn-close btn-close-white align-self-start" data-bs-dismiss="modal" aria-label="Закрыть"></button>
        </div>
        <div class="modal-body px-4 py-3">
          <form id="dosageForm">
            <div class="mb-3">
              <label for="drugSelect" class="form-label text-secondary small fw-medium">Препарат доминирования</label>
              <select class="form-select form-select-lg bg-slate-900 border-slate-800 text-light fs-6 shadow-none" id="drugSelect" required>
                <option value="" selected disabled>Выберите соединение...</option>
                <option value="Соло тестостерон">Соло тестостерон (Классика)</option>
                <option value="Метан">Метан (Завтрак чемпионов)</option>
                <option value="Тренболон">Тренболон (Ярость быка)</option>
                <option value="Дека">Дека (Суставы вечности)</option>
              </select>
            </div>
            <div class="mb-4">
              <label for="userWeight" class="form-label text-secondary small fw-medium">Текущая масса (кг)</label>
              <div class="input-group input-group-lg">
                <input type="number" class="form-control bg-slate-900 border-slate-800 text-light fs-6 shadow-none" id="userWeight" placeholder="85" required min="40" max="200">
                <span class="input-group-text bg-slate-900 border-slate-800 text-secondary fs-6">кг</span>
              </div>
            </div>
            <button type="submit" class="btn btn-success btn-lg w-100 custom-btn fw-bold">Сгенерировать назначение</button>
          </form>
          <div id="dosageResult" class="mt-4 p-3 rounded-3 bg-success-subtle text-success border border-success d-none text-center"></div>
        </div>
      </div>
    </div>
  </div>

  <footer class="py-4 text-center text-secondary small bg-black">
    <div class="container">
      <p class="mb-0">Дисклеймер: Сайт носит сугубо юмористический характер. Настоящий анаболизм — это сон, творог и базовые упражнения.</p>
    </div>
  </footer>

  <?php wp_footer(); ?>
</body>
</html>