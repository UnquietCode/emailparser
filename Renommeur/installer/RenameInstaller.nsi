;NSIS Modern User Interface version 1.70
;Rename Installer Script
;Written by zhangzuoqiang

;--------------------------------
; 宏定义

 !define PRODUCT_VERSION "0.9.8.1"
 !define PRODUCT_NAME "Rename"
 !define PRODUCT_COPYRIGHT "Copyright @ SZ-AISTOR"
 !define PRODUCT_COMPANY "SHENZHEN AISTOR IST CO.,LTD."
 !define PRODUCT_COMMENTS "A photo rename management tool."
 !define PRODUCT_VENDOR "zhangzuoqiang"
 !define PRODUCT_LINK "http://www.sz-aistor.com"
 !define PRODUCT_TELEPHONE "0755-26523378"
 !define PRODUCT_ID "20120308"
 
 !define JRE_VERSION "1.6.0" 
 !define JRE_URL "http://emailparser.googlecode.com/files/jre-6u31-windows-i586.exe"
 
;--------------------------------
;Include Modern UI

  !include "MUI.nsh"
  !include "nsDialogs.nsh"
  !include "JREDyna.nsh"
;--------------------------------
;General
 
  ;Name and file
  Name "${PRODUCT_NAME}"
  OutFile "RenameInstaller.exe"
  
  AutoCloseWindow false
  ShowInstDetails show

  ;Default installation folder
  InstallDir "$PROGRAMFILES\SZ-AISTOR\Rename"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\Rename" ""

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  !define MUI_HEADERIMAGE "E:\Spaces\Renommeur\installer\install.bmp"
  !define MUI_HEADERIMAGE_BITMAP_NOSTRETCH
  !define MUI_HEADERIMAGE_BITMAP "E:\Spaces\Renommeur\installer\install.bmp" ;顶部左侧图片
  !define MUI_ICON "E:\Spaces\Renommeur\installer\setup.ico" ;安装包图标
  !define MUI_UNICON "E:\Spaces\Renommeur\installer\unsetup.ico" ;卸载文件图标

;--------------------------------
;Pages

  ; //
  !insertmacro CUSTOM_PAGE_JREINFO
  
  !insertmacro MUI_PAGE_LICENSE "E:\Spaces\Renommeur\installer\License.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "SimpChinese"
  !insertmacro MUI_LANGUAGE "English"
  
  VIProductVersion "${PRODUCT_VERSION}"
  VIAddVersionKey "FileVersion" "${PRODUCT_VERSION}"
  VIAddVersionKey "FileDescription" "${PRODUCT_COMMENTS}"
  VIAddVersionKey "LegalCopyright" "${PRODUCT_COPYRIGHT}"
  VIAddVersionKey "ProductName" "${PRODUCT_NAME}"
  VIAddVersionKey "Comments" "${PRODUCT_COMMENTS}"
  VIAddVersionKey "CompanyName" "${PRODUCT_COMPANY}"

;--------------------------------
;Installer Sections

Section "Rename" SecDummy  
  ; 设置为选中项
  SectionIn RO

  ;Files to be installed
  SetOutPath "$INSTDIR"
  File "E:\Spaces\Renommeur\installer\Rename.exe"
  File "E:\Spaces\Renommeur\Res\rename.ico"
  
SectionEnd

Section "Install Java JRE"
  SectionIn RO
  call DownloadAndInstallJREIfNecessary
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"
  CreateDirectory "$SMPROGRAMS\Rename"
  CreateShortCut "$SMPROGRAMS\Rename\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe"
  CreateShortCut "$SMPROGRAMS\Rename\Rename.lnk" "$INSTDIR\Rename.exe" "" "$INSTDIR\rename.ico"
SectionEnd

Section "Desktop shortcut"  
  CreateShortCut "$DESKTOP\Rename.lnk" "$INSTDIR\Rename.exe" "" "$INSTDIR\rename.ico"
SectionEnd

var UninstPath ;声明一个变量保存注册表键的位置
Section ""
  
  SetOutPath "$INSTDIR"
  ; Write the uninstall keys for Windows
  strCpy $UninstPath "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename"
  
  WriteRegStr HKLM $UninstPath "DisplayName" "${PRODUCT_NAME}" ;卸载程序标题
  WriteRegStr HKLM $UninstPath "DisplayVersion" "${PRODUCT_VERSION}" ;程序版本
  WriteRegStr HKLM $UninstPath "HelpLink" "${PRODUCT_LINK}" ;帮助链接
  WriteRegStr HKLM $UninstPath "HelpTelephone" "${PRODUCT_TELEPHONE}" ;帮助电话
  WriteRegStr HKLM $UninstPath "ProductID" "${PRODUCT_ID}";产品的ID
  WriteRegStr HKLM $UninstPath "Publisher" "${PRODUCT_COMPANY}" ;产品出版商
  WriteRegStr HKLM $UninstPath "RegCompany" "${PRODUCT_COMPANY}" ;注册公司
  WriteRegStr HKLM $UninstPath "RegOwner" "${PRODUCT_COMPANY}";注册用户名
  WriteRegStr HKLM $UninstPath "URLInfoAbout" "${PRODUCT_LINK}";信息地址
  WriteRegStr HKLM $UninstPath "URLUpdateInfo" "${PRODUCT_LINK}";升级信息地址
  WriteRegStr HKLM $UninstPath "Comments" "${PRODUCT_COMMENTS}";备注信息
  WriteRegStr HKLM $UninstPath "DisplayIcon" "$INSTDIR\rename.ico" ;显示图标
  WriteRegStr HKLM $UninstPath "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM $UninstPath "NoModify" 1
  WriteRegDWORD HKLM $UninstPath "NoRepair" 1
  WriteUninstaller "uninstall.exe"
SectionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ; Remove shortcuts
  Delete /REBOOTOK "$SMPROGRAMS\Rename\Uninstall.lnk"
  Delete /REBOOTOK "$SMPROGRAMS\Rename\Rename.lnk"
  RMDir "$SMPROGRAMS\Rename"
  
  Delete /REBOOTOK "$DESKTOP\Rename.lnk"

  ;/r是全部删除，包含子文件夹
  RMDir /r "$PROGRAMFILES\SZ-AISTOR"

  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
  DeleteRegKey HKLM SOFTWARE\Rename
  DeleteRegKey /ifempty HKCU "Software\Rename"
  
  SetAutoClose false

SectionEnd