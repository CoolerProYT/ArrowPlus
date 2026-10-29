import { defineConfig } from 'vitepress'

const base = process.env.DOCS_BASE ?? '/ArrowPlus/'
const ARROW_ICON = 'https://storage.googleapis.com/coolerpromc/textures/arrowplus/diamond_arrow.png'

const MODRINTH_ICON =
  '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M256 0C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0zm28.9 364.3H227V235.5h57.9v128.8zm-29-146.5c-18.5 0-30.7-12.8-30.7-28.8 0-16.3 12.5-28.7 31.4-28.7 18.9 0 30.7 12.4 31 28.7 0 16-12.1 28.8-31.7 28.8z"/></svg>'
const CURSEFORGE_ICON =
  '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M32 96C14.3 96 0 110.3 0 128v256c0 17.7 14.3 32 32 32H480c17.7 0 32-14.3 32-32V128c0-17.7-14.3-32-32-32H32zm96 96h256c17.7 0 32 14.3 32 32s-14.3 32-32 32H128c-17.7 0-32-14.3-32-32s14.3-32 32-32zm0 96h256c17.7 0 32 14.3 32 32s-14.3 32-32 32H128c-17.7 0-32-14.3-32-32s14.3-32 32-32z"/></svg>'

export default defineConfig({
  title: 'Arrow+',
  description: 'Modular arrows for Minecraft: combine any head, stick and feather, all defined by datapacks.',
  base,
  cleanUrls: true,
  lastUpdated: true,
  srcExclude: ['README.md', 'scripts/**'],
  head: [['link', { rel: 'icon', type: 'image/png', href: ARROW_ICON }]],
  themeConfig: {
    logo: { src: ARROW_ICON, alt: '' },
    nav: [
      { text: 'Guide', link: '/getting-started' },
      { text: 'Arrows', link: '/arrows' },
      { text: 'Datapacks', link: '/datapack/arrow-data', activeMatch: '^/datapack/' },
      { component: 'VersionSwitcher' },
    ],
    sidebar: {

      '/': [
        {
          text: 'Guide',
          items: [
            { text: 'Getting started', link: '/getting-started' },
            { text: 'Arrows', link: '/arrows' },
            { text: 'Sticks', link: '/custom-sticks' },
            { text: 'Feathers', link: '/custom-feathers' },
            { text: 'Tipped arrows', link: '/tipped-arrows' },
            { text: 'Config', link: '/config' },
          ],
        },
        {
          text: 'Datapacks',
          items: [
            { text: 'Arrow data', link: '/datapack/arrow-data' },
            { text: 'Stick data', link: '/datapack/stick-data' },
            { text: 'Feather data', link: '/datapack/feather-data' },
          ],
        },
      ],

      '/v26-1/': [
        {
          text: 'Guide (26.1 – 26.3.0.1)',
          items: [
            { text: 'Getting started', link: '/v26-1/getting-started' },
            { text: 'Arrows', link: '/v26-1/arrows' },
            { text: 'Custom sticks', link: '/v26-1/custom-sticks' },
            { text: 'Custom feathers', link: '/v26-1/custom-feathers' },
            { text: 'Tipped arrows', link: '/v26-1/tipped-arrows' },
            { text: 'Config', link: '/v26-1/config' },
          ],
        },
        {
          text: 'Datapacks (26.1 – 26.3.0.1)',
          items: [
            { text: 'Arrow data', link: '/v26-1/datapack/arrow-data' },
            { text: 'Stick data', link: '/v26-1/datapack/stick-data' },
            { text: 'Feather data', link: '/v26-1/datapack/feather-data' },
          ],
        },
      ],

      '/legacy/': [
        {
          text: 'Guide (legacy)',
          items: [
            { text: 'Getting started', link: '/legacy/getting-started' },
            { text: 'Arrows', link: '/legacy/arrows' },
            { text: 'Material sticks', link: '/legacy/material-sticks' },
            { text: 'Tipped arrows', link: '/legacy/tipped-arrows' },
            { text: 'Config', link: '/legacy/config' },
          ],
        },
        {
          text: 'Datapacks (legacy)',
          items: [{ text: 'Arrow data', link: '/legacy/datapack/arrow-data' }],
        },
      ],
    },
    socialLinks: [
      { icon: 'github', link: 'https://github.com/CoolerProYT/ArrowPlus' },
      { icon: { svg: MODRINTH_ICON }, link: 'https://modrinth.com/mod/srzQqcGY', ariaLabel: 'Modrinth' },
      { icon: { svg: CURSEFORGE_ICON }, link: 'https://www.curseforge.com/minecraft/mc-mods/arrow', ariaLabel: 'CurseForge' },
    ],
    search: { provider: 'local' },
    outline: { level: [2, 3] },
    footer: { message: 'Released under the MIT License.', copyright: 'Copyright © 2024 CoolerProMC' },
  },
})
