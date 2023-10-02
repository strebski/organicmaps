#include "testing/testing.hpp"

#include "map/bookmark_helpers.hpp"

#include "platform/platform.hpp"

#include "base/scope_guard.hpp"


UNIT_TEST(KMZ_UnzipTest)
{
  std::string const kmzFile = GetPlatform().TestsDataPathForFile("kml_test_data/test.kmz");
  std::string const filePath = GetKMLorGPXPath(kmzFile);

  TEST(!filePath.empty(), ());
  SCOPE_GUARD(fileGuard, std::bind(&base::DeleteFileX, filePath));

  TEST(strings::EndsWith(filePath, "doc.kml"), (filePath));

  auto const kmlData = LoadKmlFile(filePath, KmlFileType::Text);
  TEST(kmlData.has_value(), ());

  TEST_EQUAL(kmlData->m_bookmarksData.size(), 6, ("Category wrong number of bookmarks"));

  {
    kml::BookmarkData bd = kmlData->m_bookmarksData[0];
    Bookmark const bm(std::move(bd));
    TEST_EQUAL(kml::GetDefaultStr(bm.GetName()), ("Lahaina Breakwall"), ("KML wrong name!"));
    TEST_EQUAL(bm.GetColor(), kml::PredefinedColor::Red, ("KML wrong type!"));
    TEST_ALMOST_EQUAL_ULPS(bm.GetPivot().x, -156.6777046791284, ("KML wrong org x!"));
    TEST_ALMOST_EQUAL_ULPS(bm.GetPivot().y, 21.34256685860084, ("KML wrong org y!"));
    TEST_EQUAL(bm.GetScale(), 0, ("KML wrong scale!"));
  }
  {
    kml::BookmarkData bd = kmlData->m_bookmarksData[1];
    Bookmark const bm(std::move(bd));
    TEST_EQUAL(kml::GetDefaultStr(bm.GetName()), ("Seven Sacred Pools, Kipahulu"), ("KML wrong name!"));
    TEST_EQUAL(bm.GetColor(), kml::PredefinedColor::Red, ("KML wrong type!"));
    TEST_ALMOST_EQUAL_ULPS(bm.GetPivot().x, -156.0405130750025, ("KML wrong org x!"));
    TEST_ALMOST_EQUAL_ULPS(bm.GetPivot().y, 21.12480639056074, ("KML wrong org y!"));
    TEST_EQUAL(bm.GetScale(), 0, ("KML wrong scale!"));
  }
}
