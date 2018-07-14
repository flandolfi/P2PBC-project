# %%
from scipy import stats
import numpy as np
import pandas as pd
import matplotlib as mpl
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import seaborn as sns
import os

# %% MPL SETTINGS
mpl.rcdefaults()
# sns.set(font_scale=0.8)
sns.set_style("white")
# sns.set_palette("deep")
# sns.set_palette(['w', 'gray'])
mpl.rcParams['text.usetex'] = True
mpl.rcParams['font.size'] = 13
mpl.rcParams['font.family'] = [u'serif']
mpl.rcParams['font.serif'] = [u'Computer Modern']
mpl.rcParams['lines.linewidth'] = 1
mpl.rcParams['axes.linewidth'] = 1
mpl.rcParams['figure.figsize'] = (4, 3)

# %% FIGURES DIRECTORY
outdir = "./imgs/"

if not os.path.exists(outdir):
    os.makedirs(outdir)

# %% HEATMAP
heat_pi = pd.read_csv("./data/heatmap/pi-estimation.csv", index_col=0, header=None)
heat_size = pd.read_csv("./data/heatmap/size-estimation.csv", index_col=0, header=None)
heat_epidemic = pd.read_csv("./data/heatmap/epidemic.csv", index_col=0, header=None)

side = 100

# %%
cmap = mpl.colors.ListedColormap(sns.color_palette('Blues', 256, 0.7))
fig, axes = plt.subplots(1, 2,
                         figsize=(3.5, 3),
                         gridspec_kw={ "width_ratios": [3, 0.1] })

img = axes[0].imshow(heat_size.as_matrix()[0, :].reshape((side, side)),
                     vmin=0,
                     vmax=2e-4,
                     cmap=cmap,
                     aspect='auto',
                     interpolation='none')

cb = fig.colorbar(img, cax=axes[1], ticks=[0, 1e-4, 2e-4], extend='max')
axes[1].set_yticklabels([
 "$0$",
 """$\\frac{1}{N}$""",
 """$\\frac{2}{N}$"""
])
fig.tight_layout()

# %%
def update(step):
    img = axes[0].imshow(heat_size.as_matrix()[step, :].reshape((side, side)),
                            vmin=0,
                            vmax=2e-4,
                            cmap=cmap,
                            aspect='auto',
                            interpolation='none')
    axes[0].get_xaxis().set_ticks([])
    axes[0].get_yaxis().set_ticks([])
    axes[0].set_ylabel("Estimate $1/N$")

    return fig, axes

# %%
anim = FuncAnimation(fig, update, frames=np.arange(1, 100), interval=50)
anim.save('./imgs/size.gif', dpi=80, writer='imagemagick')

# %%
cmap = mpl.colors.ListedColormap(sns.color_palette('Blues', 256, 0.7))
fig, axes = plt.subplots(1, 2,
                         figsize=(3.5, 3),
                         gridspec_kw={ "width_ratios": [3, 0.1] })

img = axes[0].imshow(heat_pi.as_matrix()[0, :].reshape((side, side)),
                     vmin=0,
                     vmax=1,
                     cmap=cmap,
                     aspect='auto',
                     interpolation='none')

cb = fig.colorbar(img, cax=axes[1], ticks=[0, 0.25*np.pi, 1])
axes[1].set_yticklabels(["$0$", """$\\frac{\pi}{4}$""", "$1$"])
fig.tight_layout()

# %%
def update(step):
    img = axes[0].imshow(heat_pi.as_matrix()[step, :].reshape((side, side)),
                         vmin=0,
                         vmax=1,
                         cmap=cmap,
                         aspect='auto',
                         interpolation='none')
    axes[0].get_xaxis().set_ticks([])
    axes[0].get_yaxis().set_ticks([])
    axes[0].set_ylabel("Estimate $\\pi/4$")

    return fig, axes

# %%
anim = FuncAnimation(fig, update, frames=np.arange(1, 100), interval=50)
anim.save('./imgs/pi.gif', dpi=80, writer='imagemagick')


# %%
cmap = mpl.colors.ListedColormap(sns.color_palette('Blues', 256, 0.7))
fig, axes = plt.subplots(1, 2,
                         figsize=(3.5, 3),
                         gridspec_kw={ "width_ratios": [3, 0.1] })

img = axes[0].imshow(heat_epidemic.as_matrix()[0, :].reshape((side, side)),
                     vmin=0,
                     vmax=1,
                     cmap=cmap,
                     aspect='auto',
                     interpolation='none')

cb = fig.colorbar(img, cax=axes[1], ticks=[0, 1])
fig.tight_layout()

# %%
def update(step):
    img = axes[0].imshow(heat_epidemic.as_matrix()[step, :].reshape((side, side)),
                         vmin=0,
                         vmax=1,
                         cmap=cmap,
                         aspect='auto',
                         interpolation='none')
    axes[0].get_xaxis().set_ticks([])
    axes[0].get_yaxis().set_ticks([])
    axes[0].set_ylabel("Estimate $max$")

    return fig, axes

# %%
anim = FuncAnimation(fig, update, frames=np.arange(1, 100), interval=50)
anim.save('./imgs/max.gif', dpi=80, writer='imagemagick')
