package maxwainer.examgui.page.manager

import javafx.scene.control.Label
import maxwainer.examgui.common.inject.delegate.define
import maxwainer.examgui.entities.Center
import maxwainer.examgui.entities.Employer
import maxwainer.examgui.module.entity.CenterService
import maxwainer.examgui.page.manager.mutator.center.CenterAddPage
import maxwainer.examgui.page.manager.mutator.center.CenterEditPage
import maxwainer.examgui.page.manager.display.ObjectViewingPage
import maxwainer.examgui.page.manager.display.SortingOption

class CenterPage(override val employer: Employer) :
  ObjectViewingPage<Center, CenterPage.CenterSortOption>(employer) {

  private val centersService by define<CenterService>()
  override val sortTypes = CenterSortOption.values()

  override val creatorPath = "new-center"
  override val editorPath = "edit-center"
  override val objects: List<Center>
    get() = centersService.all()

  override fun createAdder() = CenterAddPage(employer)
  override fun createEditor(obj: Center) = CenterEditPage(obj, employer)

  // show all Center object fields via Label
  override fun nodesFromObject(obj: Center) = listOf(
    Label("Name: ${obj.name}\t"),
    Label("Status: ${obj.status}\t"),
    Label("Pavilion count: ${obj.pavilionCount}\t"),
    Label("City: ${obj.city}\t"),
    Label("Price: ${obj.price}\t"),
    Label("Value factor: ${obj.valueFactor}\t"),
    Label("Floor count: ${obj.floorCount}\t")
  )


  override fun sort(
    option: CenterSortOption, sortValue: String, sortable: List<Center>,
  ) = when (option) {
    // filter by city
    CenterSortOption.CITY -> sortable.filter { it.city == sortValue }
    // filter by status
    CenterSortOption.STATUS -> sortable.filter { it.status == sortValue }
  }

  override fun showAvailable(
    option: CenterSortOption,
    sortable: List<Center>,
  ) = when (option) {
    // show available cities
    CenterSortOption.CITY -> sortable.map { it.city!! }.distinct()
    // show available statuses
    CenterSortOption.STATUS -> sortable.map { it.status!! }.distinct()
  }

  enum class CenterSortOption(override val displayName: String) : SortingOption {
    CITY("By city"),
    STATUS("By status"),
  }

}