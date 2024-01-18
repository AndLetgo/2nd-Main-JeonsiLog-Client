package com.example.jeonsilog.view.admin

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class AdminManagingTouchHelperCallback(private val adapter: AdminManagingRvAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN // 드래그 방향 설정
        return makeMovementFlags(dragFlags, 0) // 여기서는 아이템을 위아래로만 드래그할 수 있도록 설정
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 아이템 이동 시 호출되는 메서드
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 스와이프 시 호출되는 메서드 (사용하지 않으면 빈 메서드로 구현)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            // 드래그가 종료된 상태일 때의 처리
            val draggedPosition = viewHolder?.adapterPosition
            if (draggedPosition != RecyclerView.NO_POSITION) {
                // draggedPosition을 사용하여 드래그가 종료된 아이템의 위치에 대한 처리 수행
                // 예: 어댑터에서 아이템 순서를 업데이트하거나 다른 동작 수행
                adapter.onDragFinished()
            }
        }
    }
}